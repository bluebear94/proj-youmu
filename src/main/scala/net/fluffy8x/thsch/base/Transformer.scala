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

case class ShaderProgram(shaders: Shader*) {
  lazy val id = {
    val handle = GL20.glCreateProgram
    for (shader <- shaders) GL20.glAttachShader(handle, shader.id)
    GL20.glLinkProgram(handle)
    handle
  }
  def use() = GL20.glUseProgram(id)
  def attribute(name: String) = GL20.glGetAttribLocation(id, name)
  def delete() = GL20.glDeleteProgram(id)
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
  def fallback: Option[Shader] = None
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
  def delete() = GL20.glDeleteShader(id)
}

case object VertexShader extends Shader {
  def version = 110
  def src = s"""
    in vec3 position;
    in vec2 uv;
    in vec4 color;
    out vec2 UV;
    out vec4 Color;
    void main() {
      Color = color;
      gl_Position = vec4(position, 1.0);
    }
    """
}
case object FragmentShader extends Shader {
  def version = 110
  def src = s"""
    in vec3 Color;
    in vec2 UV;
    out vec4 outColor;
    uniform sampler2D tex;
    void main() {
      outColor = texture(tex, UV) * vec4(Color, 1.0);
    } 
    """
}