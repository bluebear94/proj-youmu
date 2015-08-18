package net.fluffy8x.thsch.entity

import net.fluffy8x.thsch.base._
import net.fluffy8x.thsch.syntax._
import org.lwjgl.opengl._
import net.fluffy8x.thsch.resource._
import scala.collection.mutable.Set
import java.nio._

/**
 * Describes an entity that can be rendered.
 */
trait Renderable extends Entity with Child[Renderable, EntityManager] {
  var position: Vector3D
  var angle: Angle
  var center: Vector3D
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
  var isVisible = true
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
  def view = parent.parent
  /**
   * Returns the upper left corner based on the current {@link CoordinateBasis}
   * and render priority.
   */
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

case class BlendMode(
  rgbEquation: Int, rgbSfactor: Int, rgbDfactor: Int,
  aEquation: Int, aSfactor: Int, aDfactor: Int) {
  def use(): Unit = {
    GL14.glBlendEquation(rgbEquation)
    //GL20.glBlendEquationSeparate(rgbEquation, aEquation)
    GL14.glBlendFuncSeparate(rgbSfactor, rgbDfactor, aSfactor, aDfactor)
  }
}
object BlendMode {
  // I have no idea
  val Alpha =
    BlendMode(
      GL14.GL_FUNC_ADD, GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA,
      GL14.GL_FUNC_ADD, GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
  val Add =
    BlendMode(
      GL14.GL_FUNC_ADD, GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE,
      GL14.GL_FUNC_ADD, GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
}

case class PrimVertex(point: Vector3D, uv: Vector2D, color: Color)

// Refer to later: glBegin glEnd glVertex* glTexCoord*

trait TPrimitive extends Renderable {
  var vertices: DoubleBuffer
  var uvs: DoubleBuffer
  var color: IntBuffer
  def vertexCount: Int
  def vertexCount_=(vc: Int): Unit
  def apply(i: Int): PrimVertex = {
    val xyz = Vector3D(
      vertices.get(3 * i),
      vertices.get(3 * i + 1),
      vertices.get(3 * i + 2)
    )
    val uv = Vector2D(
      uvs.get(i << 1),
      uvs.get((i << 1) + 1)
    )
    PrimVertex(xyz, uv, Color(color.get(i)))
  }
  def update(i: Int, v: PrimVertex): Unit = v match {
    case PrimVertex(Vector3D(x, y, z, _, _, _, _),
        Vector2D(u, v, _, _), Color(c)) => {
      vertices.put(3 * i, x)
      vertices.put(3 * i + 1, y)
      vertices.put(3 * i + 2, z)
      uvs.put(i << 1, u)
      uvs.put((i << 1) + 1, v)
      color.put(i, c)
    }
  }
  var primtype: PrimType
  var texture: SCHTexture
  var blendMode: BlendMode
  def _render() =
    if (useGL2) {
      val vbo: Int = GL15.glGenBuffers
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
      GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices,
          GL15.GL_STATIC_DRAW)
      GL20.glEnableVertexAttribArray(0)
      GL20.glVertexAttribPointer(0, 3, GL11.GL_DOUBLE, false, 0, 0)
      GL11.glDrawArrays(primtype.t, 0, vertexCount)
    } else {
      primtype.glBegin()
      texture.glSet1()
      blendMode.use()
      val len = vertexCount
      var i = 0
      while (i < len) {
        val PrimVertex(point, uv, color) = apply(i)
        val rawcoords = point +
          view.bounds.p1 +
          position
        val cr = rawcoords - center
        val r = cr.r
        val theta = cr.theta
        val coords = center.to2 + Vector2D(r, theta + angle)
        color.set()
        GL11.glTexCoord2d(uv.x, uv.y)
        GL11.glVertex2d(coords.x, coords.y)
        i += 1
      }
      GL11.glEnd()
    }
}

/**
 * A primitive object.
 */
class Primitive(
  var _primtype: PrimType,
  var _texture: SCHTexture,
  var _vertices: Array[(Color, Point2D, Point2D)],
  var _isAbsolute: Boolean = false,
  var _rotatable: Boolean = false,
  var _blendMode: BlendMode = BlendMode.Alpha) extends TPrimitive {
  def primtype = _primtype
  def texture = _texture
  def vertices = _vertices
  override def isAbsolute = _isAbsolute
  override def rotatable = _rotatable
  override def blendMode = _blendMode
}

class Sprite2D(
  var _texture: SCHTexture,
  var _source: BoundsRect,
  var _dest: BoundsRect,
  var _rotatable: Boolean = true,
  var _blendMode: BlendMode = BlendMode.Alpha) extends TPrimitive2D {
  def primtype = PrimType.Quads
  val s1 = _source.p1
    val s2 = _source.p2
    val ss1 =
      Point2D(s1.x.toDouble / texture.width, s1.y.toDouble / texture.height)
    val ss2 =
      Point2D(s2.x.toDouble / texture.width, s2.y.toDouble / texture.height)
  def texture = _texture
  def vertices = Array(
        (Color(0xFFFFFFFF), ss1, _dest.p1),
        (Color(0xFFFFFFFF), Point2D(ss2.x, ss1.y), Point2D(_dest.p2.x, _dest.p1.y)),
        (Color(0xFFFFFFFF), ss2, _dest.p2),
        (Color(0xFFFFFFFF), Point2D(ss1.x, ss2.y), Point2D(_dest.p1.x, _dest.p2.y)))
  override def isAbsolute = false
  override def rotatable = _rotatable
  override def blendMode = _blendMode
}

object Primitive2D {
  def sprite(texture: SCHTexture, source: BoundsRect, dest: BoundsRect) = {
    val s1 = source.p1
    val s2 = source.p2
    val ss1 =
      Point2D(s1.x.toDouble / texture.width, s1.y.toDouble / texture.height)
    val ss2 =
      Point2D(s2.x.toDouble / texture.width, s2.y.toDouble / texture.height)
    new Primitive2D(
      PrimType.Quads,
      texture,
      Array(
        (Color(0xFFFFFFFF), ss1, dest.p1),
        (Color(0xFFFFFFFF), Point2D(ss2.x, ss1.y), Point2D(dest.p2.x, dest.p1.y)),
        (Color(0xFFFFFFFF), ss2, dest.p2),
        (Color(0xFFFFFFFF), Point2D(ss1.x, ss2.y), Point2D(dest.p1.x, dest.p2.y))))
  }
}

/*
trait Primitive3D extends Renderable {
  var vertices: Array[(Color, Point3D, Point3D)]
*/
