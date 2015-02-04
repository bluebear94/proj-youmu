package net.fluffy8x.thsch

package object base {
  implicit def double2AngleBuilder(radians: Double): AngleBuilder =
    AngleBuilder(radians)
  implicit def double2WithVecOps(s: Double) = DoubleWithVectorOps(s)
  implicit def ordering = new Ordering[Angle] {
    override def compare(x: Angle, y: Angle): Int =
      Ordering[Double].compare(x.theta, y.theta)
  }
}