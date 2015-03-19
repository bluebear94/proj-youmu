package net.fluffy8x.thsch.base

import net.fluffy8x.thsch.syntax._

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
  lazy val r2 = x * x + y * y
  lazy val r = Math.sqrt(r2)
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
  def cross2D(v: Vector2D) = x * v.y - y * v.x
  def proj(that: Vector2D) =
	that * ((this dot that) / (that dot that))
}

object Vector2D {
  /**
   * Returns an instance of the {@link Vector2D} class from the given
   * magnitude and angle.
   */
  def fromRt(r: Double, theta: Angle) =
    Vector2D(r * theta.cos, r * theta.sin)
}

/**
 * A point in 3D space.
 * Points are assumed to be coordinate-agnostic -
 * they have only relative behavior to one another.
 * @author Fluffy8x
 */
case class Point3D(x: Double, y: Double, z: Double) {
  def +(v: Vector3D) = Point3D(x + v.x, y + v.y, z + v.z)
  def -(v: Vector3D) = Point3D(x - v.x, y - v.y, z - v.z)
  def -(v: Point3D) = Vector3D(x - v.x, y - v.y, z - v.z)
  def to2 = Point2D(x, y)
}

/**
 * A vector in 3D space.
 * Unlike points, they describe offsets; therefore they have an origin.
 * @author Fluffy8x
 */
case class Vector3D(x: Double, y: Double, z: Double) {
  lazy val r = Math.sqrt(x * x + y * y)
  lazy val rho = Math.sqrt(x * x + y * y + z * z)
  lazy val theta = Angle.atan2(y, x)
  lazy val phi = Angle.atan2(r, z)
  // Note! p.copy(x = 2)
  def withR(newR: Double) = Vector3D.fromCylindrical(newR, theta, z)
  def withTheta(newTheta: Angle) = Vector3D.fromCylindrical(r, newTheta, z)
  def withRho(newRho: Double) = Vector3D.fromSpherical(newRho, theta, phi)
  def withPhi(newPhi: Angle) = Vector3D.fromSpherical(rho, theta, newPhi)
  def +(v: Vector3D) = Vector3D(x + v.x, y + v.y, z + v.z)
  def -(v: Vector3D) = Vector3D(x - v.x, y - v.y, z - v.z)
  def unary_- = Vector3D(-x, -y, -z)
  def dot(v: Vector3D) = x * v.x + y * v.y + z * v.z
  def *(s: Double) = Vector3D(x * s, y * s, z * s)
  def /(s: Double) = Vector3D(x / s, y / s, z / s)
  def cross(v: Vector3D) =
    Vector3D(
        y * v.z - z * v.y,
        z * v.x - x * v.z,
        x * v.y - y * v.x)
  def to2 = Vector2D(x, y)
}

object Vector3D {
  /**
   * Returns an instance of the {@link Vector3D} class from the given
   * cylindrical coordinates.
   */
  def fromCylindrical(r: Double, theta: Angle, z: Double) =
    Vector3D(r * theta.cos, r * theta.sin, z)
  /**
   * Returns an instance of the {@link Vector3D} class from the given
   * spherical coordinates.
   */
  def fromSpherical(rho: Double, theta: Angle, phi: Angle) = {
    val rsp = rho * phi.sin
    Vector3D(rsp * theta.cos, rsp * theta.sin, rho * phi.cos)
  }
}

case class DoubleWithVectorOps(s: Double) extends AnyVal {
  def *(v: Vector2D) = v * s
  def *(v: Vector3D) = v * s
}

case class BoundsRect(p1: Point2D, p2: Point2D) {
  def contains(p: Point2D) = {
    between(p.x, p1.x, p2.x) && between(p.y, p1.y, p2.y)
  }
}