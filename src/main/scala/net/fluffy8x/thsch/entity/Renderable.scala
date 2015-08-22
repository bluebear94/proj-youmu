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
  var position: Vector3D = Vector3D(0, 0, 0)
  var angle: Angle = 0.radians
  var center: Vector3D = Vector3D(0, 0, 0)
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
  override def tick() {
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

case class Usage(u: Int) extends AnyVal

object Usage {
  val StaticDraw = Usage(GL15.GL_STATIC_DRAW)
  val DynamicDraw = Usage(GL15.GL_DYNAMIC_DRAW)
  val StreamDraw = Usage(GL15.GL_STREAM_DRAW)
}

case class PrimVertex(point: Vector3D, uv: Vector2D, color: Color)

// Refer to later: glBegin glEnd glVertex* glTexCoord*

class Primitive extends Renderable {
  var vdata: DoubleBuffer = DoubleBuffer.allocate(64)
  var elemBuffer: IntBuffer = IntBuffer.allocate(64)
  private var vc = 0
  private var ec = 0
  val ENTRIES_PER_VERTEX = 9
  def vertexCount: Int = vc
  def vertexCount_=(vc: Int): Unit = {
    this.vc = vc
    val ec = vc * ENTRIES_PER_VERTEX
    if (ec <= vdata.capacity) vdata.limit(ec)
    else {
      val newVData = DoubleBuffer.allocate(vdata.capacity << 1)
      newVData.limit(ec)
      vdata = newVData
    }
  }
  def elemCount: Int = ec
  def elemCount_=(ec: Int): Unit = {
    var i = this.ec
    this.ec = ec
    if (ec <= elemBuffer.capacity) elemBuffer.limit(ec)
    else {
      val newEBuffer = IntBuffer.allocate(elemBuffer.capacity << 1)
      newEBuffer.limit(ec)
      elemBuffer = newEBuffer
    }
    while (i < ec) {
      elemBuffer.put(i, i)
      i += 1
    }
  }
  def apply(i: Int): PrimVertex = {
    val offset = ENTRIES_PER_VERTEX * i
    val xyz = Vector3D(
      vdata.get(offset),
      vdata.get(offset + 1),
      vdata.get(offset + 2)
    )
    val uv = Vector2D(
      vdata.get(offset + 3),
      vdata.get(offset + 4)
    )
    val color = Color(
      (255 * vdata.get(offset + 7)).toInt,
      (255 * vdata.get(offset + 6)).toInt,
      (255 * vdata.get(offset + 5)).toInt,
      (255 * vdata.get(offset + 8)).toInt
    )
    PrimVertex(xyz, uv, color)
  }
  def update(i: Int, v: PrimVertex): Unit = v match {
    case PrimVertex(Vector3D(x, y, z, _, _, _, _),
        Vector2D(u, v, _, _), c) => {
      val offset = ENTRIES_PER_VERTEX * i
      vdata.put(offset, x)
      vdata.put(offset + 1, y)
      vdata.put(offset + 2, z)
      vdata.put(offset + 3, u)
      vdata.put(offset + 4, v)
      vdata.put(offset + 5, c.b / 255.0)
      vdata.put(offset + 6, c.g / 255.0)
      vdata.put(offset + 7, c.r / 255.0)
      vdata.put(offset + 8, c.a / 255.0)
    }
  }
  def usage = Usage.StreamDraw
  var primtype: PrimType = PrimType.Triangles
  var texture: SCHTexture = SCHTexture.white
  var blendMode: BlendMode = BlendMode.Alpha
  // Begin GL3.x specific fields
  private var vbo: Int = -1
  private var vao: Int = -1
  private var ebo: Int = -1
  private var shaderProgram: ShaderProgram = null
  private var texHandle: Int = -1
  // End GL3.x specific fields
  // This is MADNESS.
  override def _register(m: EntityManager) = {
    super._register(m)
    println("initialize")
    if (useGL3) {
      vbo = GL15.glGenBuffers
      ebo = GL15.glGenBuffers
      vao = GL30.glGenVertexArrays
      texHandle = GL11.glGenTextures
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
      GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo)
      GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vdata, usage.u)
      GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, elemBuffer, usage.u)
      GL30.glBindVertexArray(vao)
      shaderProgram = ShaderProgram(VertexShader, FragmentShader)
      GL30.glBindFragDataLocation(shaderProgram.id, 0, "outColor")
      val positionAttribute = shaderProgram.attribute("position")
      GL20.glEnableVertexAttribArray(positionAttribute)
      GL20.glVertexAttribPointer(positionAttribute,
          3, GL11.GL_DOUBLE, false, 6 * 8, 0)
      val colorAttribute = shaderProgram.attribute("color")
      GL20.glEnableVertexAttribArray(colorAttribute)
      GL20.glVertexAttribPointer(colorAttribute,
          GL12.GL_BGRA, GL11.GL_DOUBLE, false, 5 * 8, 0)
      val textureAttribute = shaderProgram.attribute("uv")
      GL20.glEnableVertexAttribArray(textureAttribute)
      GL20.glVertexAttribPointer(textureAttribute,
          2, GL11.GL_DOUBLE, false, 7 * 8, 0)
    }
  }
  def _render() =
    println("tick")
    if (useGL3) {
      texture.glSet()
      if (shaderProgram != null) shaderProgram.use()
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
      GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo)
      GL11.glDrawElements(primtype.t, vertexCount, GL11.GL_UNSIGNED_INT, 0)
    } else {
      primtype.glBegin()
      texture.glSet()
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
        val coords = center.to2 + Vector2D.polar(r, theta + angle)
        color.set()
        GL11.glTexCoord2d(uv.x, uv.y)
        GL11.glVertex2d(coords.x, coords.y)
        i += 1
      }
      GL11.glEnd()
    }
  override def onDelete(m: EntityManager) = {
    super.onDelete(m)
    shaderProgram.delete()
    GL15.glDeleteBuffers(vbo)
    GL15.glDeleteBuffers(ebo)
    GL30.glDeleteVertexArrays(vao)
  }
}
/*
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
}*/

/*
trait Primitive3D extends Renderable {
  var vertices: Array[(Color, Point3D, Point3D)]
*/
