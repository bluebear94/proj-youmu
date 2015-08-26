package net.fluffy8x.thsch.base

import java.nio.FloatBuffer
import org.lwjgl._

/**
 * @author bluebear94
 * For certain reasons we must use floats instead of doubles.
 */
class Matrix(elems: FloatBuffer) {
  def apply(row: Int, col: Int) = elems.get((row << 2) + col)
  def *(m: Matrix) = {
    var newElems = BufferUtils.createFloatBuffer(16)
    var row = 0
    var col = 0
    while (row < 4) {
      while (col < 4) {
        var ctr = 0
        var res = 0.0f
        while (ctr < 4) {
          res += this(row, ctr) * m(ctr, col)
          ctr += 1
        }
        newElems.put((row << 2) + col, res)
      }
      col = 0
      row += 1
    }
    new Matrix(newElems)
  }
  def *(v: Vector4D) = {
    def elem(i: Int) =
      v.x * this(i, 0) + v.y * this(i, 1) +
      v.z * this(i, 2) + v.w * this(i, 3)
    Vector4D(elem(0), elem(1), elem(2), elem(3))
  }
  def toFloatBuffer = elems
}

object Matrix {
  private val idbuff = BufferUtils.createFloatBuffer(16)
  BufferUtils.zeroBuffer(idbuff)
  for (i <- 0 until 4) idbuff.put(5 * i, 1.0f)
  val identity = new Matrix(idbuff)
  def generate(f: (Int, Int) => Float) = {
    var newElems = BufferUtils.createFloatBuffer(16)
    var row = 0
    var col = 0
    while (row < 4) {
      while (col < 4) {
        newElems.put((row << 2) + col, f(row, col))
      }
      col = 0
      row += 1
    }
    new Matrix(newElems)
  }
  def scale(xf: Float, yf: Float, zf: Float) =
    generate { (row, col) =>
      if (row == col) Array(xf, yf, zf, 1)(row)
      else 0
    }
  def translate(x: Float, y: Float, z: Float) =
    generate { (row, col) =>
      if (row == col) 1
      else if (col == 3) Array(x, y, z)(row)
      else 0
    }
}