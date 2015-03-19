package net.fluffy8x.thsch.base

import org.lwjgl.opengl.GL11
import java.nio.FloatBuffer

/**
 * A color plus alpha. 8 bytes per channel, and laid out identically
 * to AWT's version. We choose not to use AWT's version since it
 * is an ordinary class, while this one extends {@link AnyVal}, reducing
 * the overhead of a reference class.
 * @author Fluffy8x
 */
case class Color(rgba: Int) extends AnyVal {
  def a = (rgba >>> 24).toByte
  def r = (rgba >>> 16).toByte
  def g = (rgba >>> 8).toByte
  def b = rgba.toByte
  def toAWT = new java.awt.Color(rgba)
  def set() = GL11.glColor4ub(r, b, g, a)
}

object Color {
  def apply(r: Int, g: Int, b: Int, a: Int): Color =
    Color((a << 24) | (r << 16) | (g << 8) | b)
  def apply(r: Int, g: Int, b: Int): Color = apply(r, g, b, 255)
  def get = {
    val buf = FloatBuffer.allocate(4)
    GL11.glGetFloat(GL11.GL_CURRENT_COLOR, buf)
    Color(
      (buf.get(0) * 255).toByte,
      (buf.get(1) * 255).toByte,
      (buf.get(2) * 255).toByte,
      (buf.get(3) * 255).toByte
    )
  }
  def fromAWT(c: java.awt.Color) = Color(c.getRGB)
}