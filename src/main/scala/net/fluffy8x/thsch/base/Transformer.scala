package net.fluffy8x.thsch.base

import org.lwjgl.opengl._

/**
 * @author bluebear94
 */
trait Transformer {
  def matrix: Matrix
}

case object IdentityTransformer extends Transformer {
  def matrix = Matrix.identity
}

/**
 * Indicates an error in compiling a shader.
 */
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
  lazy val id: Int = {
    val handle = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)
    val fullSource = s"#version $version\n$src"
    GL20.glShaderSource(handle, fullSource)
    GL20.glCompileShader(handle)
    if (GL20.glGetShaderi(handle, GL20.GL_COMPILE_STATUS) == 0){
      fallback match {
        case Some(theFallback) =>
          theFallback.id
        case None =>
          throw new ShaderCompileFailedError(handle, fullSource,
              GL20.glGetShaderInfoLog(handle))
      }
    }
    else handle
  }
  def delete() = GL20.glDeleteShader(id)
}

case object VertexShader extends Shader {
  def version = 120
  def src = s"""
    attribute vec3 position;
    attribute vec2 uv;
    attribute vec4 color;
    varying vec2 fUV;
    varying vec4 fColor;
    uniform mat4 mvp;
    void main() {
      gl_Position = mvp * vec4(position, 1.0);
      fColor = color;
      fUV = uv;
    }
    """
}
case object FragmentShader extends Shader {
  def version = 120
  def src = s"""
    varying vec4 fColor;
    varying vec2 fUV;
    uniform sampler2D tex;
    void main() {
      vec2 resUV = vec2(fUV.x, 1.0 - fUV.y);
      gl_FragColor = fColor * texture2D(tex, resUV);
    } 
    """
}