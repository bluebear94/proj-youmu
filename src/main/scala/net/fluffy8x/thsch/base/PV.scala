package net.fluffy8x.thsch.base

/**
 * A point in 2D space.
 * Points are assumed to be coordinate-agnostic -
 * they have only relative behavior to one another.
 * @author Fluffy8x
 */
case class Point2D(x: Double, y: Double) {
  def +(v: Vector2D) = Point2D(x + v.x, y + v.y)
  def -(v: Vector2D) = Point2D(x - v.x, y - v.y)
  def -(v: Point2D) = Vector2D(x - v.x, y - v.y)
}

/**
 * A vector in 2D space.
 * Unlike points, they describe offsets; therefore they have an origin.
 * @author Fluffy8x
 */
case class Vector2D(x: Double, y: Double) {
  lazy val r = Math.sqrt(x * x + y * y)
  lazy val theta = Angle.atan2(y, x)
  // Note! p.copy(x = 2)
  def withR(newR: Double) = Vector2D.fromRt(newR, theta)
  def withTheta(newTheta: Angle) = Vector2D.fromRt(r, newTheta)
  def +(v: Vector2D) = Vector2D(x + v.x, y + v.y)
  def -(v: Vector2D) = Vector2D(x - v.x, y - v.y)
  def unary_- = Vector2D(-x, -y)
  def dot(v: Vector2D) = x * v.x + y * v.y
  def *(s: Double) = Vector2D(x * s, y * s)
  def /(s: Double) = Vector2D(x / s, y / s)
}

object Vector2D {
  /**
   * Returns an instance of the {@link Vector2D} class from the given
   * magnitude and angle.
   */
  def fromRt(r: Double, theta: Angle) =
    Vector2D(r * theta.cos, r * theta.sin)
}

case class DoubleWithVectorOps(s: Double) extends AnyVal {
  def *(v: Vector2D) = v * s
}