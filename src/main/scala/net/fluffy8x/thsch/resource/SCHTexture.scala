package net.fluffy8x.thsch.resource

import java.nio._
import org.lwjgl.opengl._
import org.lwjgl.system.MathUtil
import com.sksamuel.scrimage._
import java.io.File

/**
 * A texture resource.
 * While using non-power-of-two dimensions is allowed,
 * it is not advised if you intend to support older
 * GPUs that do not support them.
 */
class SCHTexture(w: Int, h: Int) {
  val width = w
  val height = h
  val pixels = IntBuffer.allocate(width * height)
  /**
   * The time at which this texture was last used.
   */
  var lastUsed = System.nanoTime
  /**
   * Sets this as the current texture used by OpenGL.
   */
  def glSet() = {
    GL11.glTexImage2D(
      GL11.GL_TEXTURE_2D,
      0,
      4,
      width,
      height,
      0,
      GL12.GL_BGRA,
      GL12.GL_UNSIGNED_INT_8_8_8_8_REV,
      pixels
    )
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
        GL11.GL_REPEAT)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
        GL11.GL_REPEAT)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
        GL11.GL_LINEAR)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
        GL11.GL_LINEAR)
    lastUsed = System.nanoTime
  }
  def size = (width * height) << 2
}

object SCHTexture {
  def read(f: File) = {
    val im = Image(f)
    val w = im.width
    val h = im.height
    val tex = new SCHTexture(w, h)
    var j = 0
    var i = 0
    var k = 0
    while (j < h) { // faster than Scala's for loop
      i = 0
      while (i < w) {
        tex.pixels.put(k + i, im.pixel(j, i))
      }
      k += tex.width
    }
    tex
  }
  val white = new SCHTexture(1, 1)
  white.pixels.put(0, -1)
}