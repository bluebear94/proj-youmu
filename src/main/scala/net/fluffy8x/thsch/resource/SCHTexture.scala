package net.fluffy8x.thsch.resource

import java.nio._
import org.lwjgl.opengl._
import org.lwjgl.system.MathUtil
import com.sksamuel.scrimage._
import java.io.File

class SCHTexture(w: Int, h: Int) {
  // round to powers of two
  val width = MathUtil.mathRoundPoT(w)
  val height = MathUtil.mathRoundPoT(h)
  val pixels = IntBuffer.allocate(width * height)
  var lastUsed = System.nanoTime
  def glSet1() = {
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
    lastUsed = System.nanoTime
  }
  def glSet2() = {
    
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
}