package net.fluffy8x.thsch.base

import org.lwjgl.opengl._

object Diagnostics {
  val c = GLContext.createFromCurrent
  def isDX10 = {
    c.getCapabilities.OpenGL30 ||
      GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE) >= 8192
  }
  def npotSupportLevel = {
    if (isDX10) NPOTSupportLevel.Full
    else if (c.getCapabilities.OpenGL20 ||
      c.getCapabilities.GL_ARB_texture_non_power_of_two)
      NPOTSupportLevel.Partial
      else NPOTSupportLevel.None
  }
  def main(args: Array[String]) =
    println(s"$npotSupportLevel support for NPOT (non-power-of-two) textures")
}

case class NPOTSupportLevel(l: Byte) {
  override def toString = Array("None", "Partial", "Full").apply(l)
}

object NPOTSupportLevel {
  val Full = NPOTSupportLevel(2)
  val Partial = NPOTSupportLevel(1)
  val None = NPOTSupportLevel(0)
}