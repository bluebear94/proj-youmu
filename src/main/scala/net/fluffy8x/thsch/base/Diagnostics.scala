package net.fluffy8x.thsch.base

import org.lwjgl.opengl._
import net.fluffy8x.thsch.lwjgl._
import net.fluffy8x.thsch.entity._
import net.fluffy8x.thsch.base._

object Diagnostics extends LWJGLApplication {
  def isDX10 = {
    c.OpenGL30 ||
      GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE) >= 8192
  }
  def npotSupportLevel = {
    if (isDX10) NPOTSupportLevel.Full
    else if (c.OpenGL20 ||
      c.GL_ARB_texture_non_power_of_two)
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
    val color = Color(32, 255, 64)
    triangle(0) = PrimVertex(Vector3D(0.5, -1, 0), Vector2D(0, 0), color)
    triangle(1) = PrimVertex(Vector3D(-1, 1, 0), Vector2D(0, 1), color)
    triangle(2) = PrimVertex(Vector3D(1, 1, 0), Vector2D(1, 1), color)
    view = new View(
        BoundsRect(Vector2D(0, 0), Vector2D(640, 480)),
        IdentityTransformer,
        None,
        None
    )
    em = new EntityManager
    view.register(em)
    triangle.register(em)
    for (i <- 0 until 3) println(triangle(i))
  }
  def work() = {
    em.renderAll()
    /*GL11.glBegin(GL11.GL_QUADS)
    GL11.glColor3d(1.0, 1.0, 1.0)
    GL11.glVertex2d(-0.5, -0.5)
    GL11.glVertex2d(-0.5, 0.5)
    GL11.glVertex2d(0.5, 0.5)
    GL11.glVertex2d(0.5, -0.5)*/
    GL11.glEnd()
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