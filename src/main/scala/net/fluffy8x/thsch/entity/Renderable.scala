package net.fluffy8x.thsch.entity

import net.fluffy8x.thsch.base._
import org.lwjgl.opengl._
import net.fluffy8x.thsch.resource._

/**
 * Describes an entity that can be rendered.
 */
trait Renderable extends Entity with Child[Renderable, EntityManager] {
  var position: Point3D
  var angle: Angle
  def render() {
    if (isVisible) _render()
  }
  protected def _render(): Unit
  
  var isVisible = false
  def basis: CoordinateBasis
  def renderPriority: Double
  /**
   * Returns the upper left corner based on the current {@link CoordinateBasis}
   * and render priority.
   */
  def upperLeft = basis match {
    case CoordinateBasis.Window => Point2D(0, 0)
    case CoordinateBasis.Frame => {
      val window = parent.parent.parent
      Point2D(window.stageX, window.stageY)
    }
    case CoordinateBasis.Auto => {
      if (renderPriority >= 0.2 && renderPriority <= 0.8) {
        val window = parent.parent.parent
        Point2D(window.stageX, window.stageY)
      } else Point2D(0, 0)
    }
  }
  abstract override def tick() {
    super.tick()
    render()
  }
}

case class PrimType(t: Int) extends AnyVal {
  def glBegin() = GL11.glBegin(t)
}
object PrimType {
  val Points = PrimType(GL11.GL_POINTS)
  val Lines = PrimType(GL11.GL_LINES)
  val LineStrip = PrimType(GL11.GL_LINE_STRIP)
  val LineLoop = PrimType(GL11.GL_LINE_LOOP)
  val Triangles = PrimType(GL11.GL_TRIANGLES)
  val TriangleStrip = PrimType(GL11.GL_TRIANGLE_STRIP)
  val TriangleFan = PrimType(GL11.GL_TRIANGLE_FAN)
  val Quads = PrimType(GL11.GL_QUADS)
  val QuadStrip = PrimType(GL11.GL_QUAD_STRIP)
  val Polygon = PrimType(GL11.GL_POLYGON)
}


// Uncomment this when we find a way to represent textures
// Refer to later: glBegin glEnd glVertex* glTexCoord*
// 
class Primitive2D(
  var primtype: PrimType,
  var texture: SCHTexture,
  var vertices: Array[(Color, Point2D, Point2D)]
) extends Renderable {
  def _render() {
    primtype.glBegin()
    texture.glSet()
    val len = vertices.length
    var i = 0
    while (i < len) {
      val (col, texCoords, coords) = vertices(i)
      col.set()
      GL11.glTexCoord2d(texCoords.x, texCoords.y)
      GL11.glVertex2d(coords.x, coords.y)
      i += 1
    }
  }
  protected def _register(m: EntityManager) {
    m.miscPrimitives += this
  }
}
/*
trait Primitive3D extends Renderable {
  var vertices: Array[(Color, Point3D, Point3D)]
*/
