package net.fluffy8x.thsch.base

/**
 * @author bluebear94
 */
class Matrix(elems: Array[Double]) {
  def apply(row: Int, col: Int) = elems((row << 2) + col)
  def *(m: Matrix) = {
    var newElems = new Array[Double](16)
    var row = 0
    var col = 0
    while (row < 4) {
      while (col < 4) {
        var ctr = 0
        var res = 0.0
        while (ctr < 4) {
          res += this(row, ctr) * m(ctr, col)
          ctr += 1
        }
        newElems((row << 2) + col) = res
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
}

object Matrix {
  val identity = new Matrix(Array(
    1.0, 0.0, 0.0, 0.0,
    0.0, 1.0, 0.0, 0.0,
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
  ))
}