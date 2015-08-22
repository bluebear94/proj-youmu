package net.fluffy8x.thsch.base

import org.lwjgl.opengl._
import net.fluffy8x.thsch.lwjgl._
import net.fluffy8x.thsch.entity._
import net.fluffy8x.thsch.base._

object Diagnostics extends LWJGLApplication {
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
  var triangle: Primitive = null
  var view: View = null
  var em: EntityManager = null
  def myinit() = {
    println(s"$npotSupportLevel support for NPOT (non-power-of-two) textures")
    println(s"OpenGL version ${GL11.glGetString(GL11.GL_VERSION)}")
    triangle = new Primitive
    triangle.vertexCount = 3
    triangle.elemCount = 3
    triangle(0) = PrimVertex(Vector3D(0.5, -1, 0), Vector2D(0, 0), Color(-1))
    triangle(1) = PrimVertex(Vector3D(-1, 1, 0), Vector2D(0, 1), Color(-1))
    triangle(2) = PrimVertex(Vector3D(1, 1, 0), Vector2D(1, 1), Color(-1))
    println(triangle(1))
    for (i <- 0 until 27) println(triangle.vdata.get(i))
    view = new View(
        BoundsRect(Vector2D(0, 0), Vector2D(640, 480)),
        IdentityTransformer,
        None,
        None
    )
    em = new EntityManager
    view.register(em)
    triangle.register(em)
  }
  def work() = {
    em.renderAll()
    true
  }
  protected def width: Int = 640
  protected def height: Int = 480
  protected def title: String = "Diagnostics"
}

case class NPOTSupportLevel(l: Byte) {
  override def toString = Array("No", "Partial", "Full").apply(l)
}

object NPOTSupportLevel {
  val Full = NPOTSupportLevel(2)
  val Partial = NPOTSupportLevel(1)
  val None = NPOTSupportLevel(0)
}