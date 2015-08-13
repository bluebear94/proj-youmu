package net.fluffy8x.thsch

import org.lwjgl.opengl.GL

package object base {
  implicit def double2AngleBuilder(radians: Double): AngleBuilder =
    AngleBuilder(radians)
  implicit def double2WithVecOps(s: Double) = DoubleWithVectorOps(s)
  implicit def ordering = new Ordering[Angle] {
    override def compare(x: Angle, y: Angle): Int =
      Ordering[Double].compare(x.theta, y.theta)
  }
  implicit def pairToPoint(p: (Double, Double)) = Vector2D(p._1, p._2)
  implicit def tripleToPoint(p: (Double, Double, Double)) =
    Vector3D(p._1, p._2, p._3)
  implicit def point2DTo3D(p: Vector2D) = Vector3D(p.x, p.y, 0)
}