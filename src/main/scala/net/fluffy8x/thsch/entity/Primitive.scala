package net.fluffy8x.thsch.entity

import net.fluffy8x.thsch.base._
import net.fluffy8x.thsch.syntax._
import org.lwjgl._
import org.lwjgl.opengl._
import net.fluffy8x.thsch.resource._
import scala.collection.mutable.Set
import java.nio._


case class PrimVertex(point: Vector3D, uv: Vector2D, color: Color)

// Refer to later: glBegin glEnd glVertex* glTexCoord*

class Primitive extends Renderable {
  var vdata: DoubleBuffer = BufferUtils.createDoubleBuffer(64)
  var elemBuffer: IntBuffer = BufferUtils.createIntBuffer(64)
  private var vc = 0
  private var ec = 0
  val ENTRIES_PER_VERTEX = 9
  val VERTEX_SIZE = ENTRIES_PER_VERTEX << 3
  def vertexCount: Int = vc
  def vertexCount_=(vc: Int): Unit = {
    this.vc = vc
    val ec = vc * ENTRIES_PER_VERTEX
    if (ec <= vdata.capacity) vdata.limit(ec)
    else {
      val newVData = BufferUtils.createDoubleBuffer(vdata.capacity << 1)
      newVData.limit(ec)
      vdata = newVData
    }
    if (vc >= ec) elemCount = vc
  }
  def elemCount: Int = ec
  def elemCount_=(ec: Int): Unit = {
    var i = this.ec
    this.ec = ec
    if (ec <= elemBuffer.capacity) elemBuffer.limit(ec)
    else {
      val newEBuffer = BufferUtils.createIntBuffer(elemBuffer.capacity << 1)
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
  def refreshBuffers() = if (useGL3) {
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vdata, usage.u)
    GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, elemBuffer, usage.u)
  }
  // Begin GL3.x specific fields
  private var vbo: Int = -1
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
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
      GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vdata, usage.u)
      ebo = GL15.glGenBuffers
      GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo)
      GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, elemBuffer, usage.u)
      texHandle = GL11.glGenTextures
      GL11.glBindTexture(GL11.GL_TEXTURE_2D, texHandle)
      shaderProgram = ShaderProgram(VertexShader, FragmentShader)
      GL30.glBindFragDataLocation(shaderProgram.id, 0, "gl_FragColor")
      /*val positionAttribute = shaderProgram.attribute("position")
      GL20.glEnableVertexAttribArray(positionAttribute)
      GL20.glVertexAttribPointer(positionAttribute,
          3, GL11.GL_DOUBLE, false, VERTEX_SIZE, 0)
      val colorAttribute = shaderProgram.attribute("color")
      GL20.glEnableVertexAttribArray(colorAttribute)
      GL20.glVertexAttribPointer(colorAttribute,
          GL12.GL_BGRA, GL11.GL_DOUBLE, false, VERTEX_SIZE, 5 * 8)
      val textureAttribute = shaderProgram.attribute("uv")
      GL20.glEnableVertexAttribArray(textureAttribute)
      GL20.glVertexAttribPointer(textureAttribute,
          2, GL11.GL_DOUBLE, false, VERTEX_SIZE, 3 * 8)*/
    }
    GL11.glEnable(GL11.GL_BLEND)
  }
  def _render() = {
    if (useGL3) {
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
      GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo)
      GL11.glBindTexture(GL11.GL_TEXTURE_2D, texHandle)
      texture.glSet()
      blendMode.use()
      shaderProgram.use()
      val positionAttribute = shaderProgram.attribute("position")
      GL20.glEnableVertexAttribArray(positionAttribute)
      GL20.glVertexAttribPointer(positionAttribute,
          3, GL11.GL_DOUBLE, false, VERTEX_SIZE, 0)
      val colorAttribute = shaderProgram.attribute("color")
      GL20.glEnableVertexAttribArray(colorAttribute)
      GL20.glVertexAttribPointer(colorAttribute,
          GL12.GL_BGRA, GL11.GL_DOUBLE, false, VERTEX_SIZE, 5 * 8)
      val textureAttribute = shaderProgram.attribute("uv")
      GL20.glEnableVertexAttribArray(textureAttribute)
      GL20.glVertexAttribPointer(textureAttribute,
          2, GL11.GL_DOUBLE, false, VERTEX_SIZE, 3 * 8)
      val mvpUniform = shaderProgram.uniform("mvp")
      GL20.glUniformMatrix4fv(mvpUniform, false, Matrix.identity.toFloatBuffer)
      //GL11.glDrawElements(primtype.t, elemBuffer)
      GL11.glDrawElements(primtype.t, elemCount, GL11.GL_UNSIGNED_INT, 0)
    } else {
      texture.glSet()
      blendMode.use()
      primtype.glBegin()
      val len = elemCount
      var i = 0
      while (i < len) {
        val PrimVertex(point, uv, color) = apply(elemBuffer.get(i))
        val rawcoords = point +
          view.bounds.p1 +
          position
        val cr = rawcoords - center
        val r = cr.r
        val theta = cr.theta
        val coords = center + Vector3D.cylindrical(r, theta + angle, 0)
        color.set()
        GL11.glTexCoord2d(uv.x, uv.y)
        GL11.glVertex3d(coords.x, coords.y, coords.z)
        i += 1
      }
      GL11.glEnd()
    }
  }
  override def onDelete(m: EntityManager) = {
    super.onDelete(m)
    shaderProgram.delete()
    GL15.glDeleteBuffers(vbo)
    GL15.glDeleteBuffers(ebo)
    GL11.glDeleteTextures(texHandle)
    //GL30.glDeleteVertexArrays(vao)
  }
}