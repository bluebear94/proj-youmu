package net.fluffy8x.thsch.entity

import net.fluffy8x.thsch.base._
import org.lwjgl.opengl._
import net.fluffy8x.thsch.resource._
import scala.collection.mutable.Set

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
  protected def _register(m: EntityManager) {
    val r = m.renderables
    try {
      val theSet = r(renderPriority)
      theSet += this
    } catch {
      case e: NoSuchElementException => {
    	m.renderables = r.updated(renderPriority, Set(this))
      }
    }
  }
  var isVisible = false
  def basis: CoordinateBasis
  private var rp = 0.0
  def renderPriority: Double = rp
  def renderPriority_=(_rp: Double) = {
    val r = manager.renderables
    val theSet = r(rp)
    theSet -= this
    rp = _rp
    try {
      val theSet = r(rp)
      theSet += this
    } catch {
      case e: NoSuchElementException => {
    	manager.renderables = r.updated(rp, Set(this))
      }
    }
  }
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
  override def delete() {
    super.delete()
    val r = manager.renderables
    val theSet = r(renderPriority)
    theSet -= this
  }
}

/**
 * A primitive drawing mode.
 */
case class PrimType(t: Int) extends AnyVal {
  /**
   * Calls <code>GL11.glBegin</code> using the current object's t.
   */
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


// Refer to later: glBegin glEnd glVertex* glTexCoord*
/**
 * A 2D primitive object.
 */
class Primitive2D(
  var primtype: PrimType,
  var texture: SCHTexture,
  var vertices: Array[(Color, Point2D, Point2D)],
  var isAbsolute: Boolean = false
) extends Renderable {
  protected def _render {
    if (isAbsolute) _renderAbs()
    else _renderRel()
  }
  protected def _renderAbs() {
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
    GL11.glEnd()
  }
  protected def _renderRel() {
    primtype.glBegin()
    texture.glSet()
    val len = vertices.length
    var i = 0
    while (i < len) {
      val (col, texCoords, c) = vertices(i)
      val coords = c + 
      	(upperLeft - Point2D(0, 0)) +
      	(position.to2 - Point2D(0, 0))
      col.set()
      GL11.glTexCoord2d(texCoords.x, texCoords.y)
      GL11.glVertex2d(coords.x, coords.y)
      i += 1
    }
    GL11.glEnd()
  }
}
/*
trait Primitive3D extends Renderable {
  var vertices: Array[(Color, Point3D, Point3D)]
*/
