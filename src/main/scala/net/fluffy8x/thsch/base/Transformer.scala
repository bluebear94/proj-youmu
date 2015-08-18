package net.fluffy8x.thsch.base

import org.lwjgl.opengl._

/**
 * @author bluebear94
 */
trait Transformer {
  def transform(p: Vector3D): Vector2D
}

class ShaderCompileFailedError(
    handle: Int, source: String, log: String)
    extends Error {
  override def getMessage =
    s"Failed to compile shader with handle $handle\n" +
      s"source:\n$source\nlog:\n$log"
}

/**
 * A shader object.
 * @author bluebear94
 */
trait Shader {
  /**
   * The lowest version this shader supports.
   */
  def version: Int
  /**
   * The GLSL source of the shader, not including the
   * version declaration.
   */
  def src: String
  /**
   * Specification of a fallback shader if
   * this one is not supported.
   * @return <code>Some(shader)</code> if this we should resort
   * to a fallback if the GLSL version is less than
   * <code>version</code>, or <code>None</code> if we should
   * give up and not apply the shader
   */
  def fallback: Option[Shader]
  lazy val id = {
    val handle = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)
    val fullSource = s"#version $version\n$src"
    GL20.glShaderSource(handle, fullSource)
    GL20.glCompileShader(handle)
    if (GL20.glGetShaderi(handle, GL20.GL_COMPILE_STATUS) == 0)
      throw new ShaderCompileFailedError(handle, fullSource,
        GL20.glGetShaderInfoLog(handle))
    handle
  }
}