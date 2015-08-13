package net.fluffy8x.thsch.base

import net.fluffy8x.thsch.syntax._

/**
 * A vector in 2D space.
 * As of version 0.2, points and vectors are unified.
 * @author Fluffy8x
 */
case class Vector2D(x: Double, y: Double, r: Double, t: Angle) {
  // Note! p.copy(x = 2)
  def withX(newX: Double) = Vector2D(newX, y)
  def withY(newY: Double) = Vector2D(x, newY)
  def withR(newR: Double) = Vector2D(newR, t)
  def withTheta(newTheta: Angle) = Vector2D(r, newTheta)
  def +(v: Vector2D) = Vector2D(x + v.x, y + v.y)
  def -(v: Vector2D) = Vector2D(x - v.x, y - v.y)
  def unary_- = Vector2D(-x, -y, r, 180.degrees + t)
  def dot(v: Vector2D) = x * v.x + y * v.y
  def *(s: Double) =
    if (s >= 0) Vector2D(x * s, y * s, r * s, t)
    else Vector2D(x * s, y * s, -r * s, 180.degrees + t)
  def /(s: Double) = this * (1 / s)
  def cross2D(v: Vector2D) = x * v.y - y * v.x
  def proj(that: Vector2D) =
	  that * ((this dot that) / (that dot that))
}

object Vector2D {
  /**
   * Returns an instance of the {@link Vector2D} class from the given
   * X and Y.
   */
  def apply(x: Double, y: Double): Vector2D =
    Vector2D(x, y, Math.sqrt(x * x + y * y), Angle.atan2(y, x))
  /**
   * Returns an instance of the {@link Vector2D} class from the given
   * magnitude and angle.
   */
  def apply(r: Double, theta: Angle): Vector2D =
    Vector2D(r * theta.cos, r * theta.sin, r, theta)
}

/**
 * A vector in 3D space.
 * As of 0.2, points and vectors are unified.
 * @author Fluffy8x
 */
case class Vector3D(x: Double, y: Double, z: Double, r: Double, rho: Double, theta: Angle, phi: Angle) {
  // Note! p.copy(x = 2)
  def withX(newX: Double) = Vector3D(newX, y, z)
  def withY(newY: Double) = Vector3D(x, newY, z)
  def withZ(newZ: Double) = Vector3D(x, y, newZ)
  def withR(newR: Double) = Vector3D(newR, theta, z)
  def withTheta(newTheta: Angle) = Vector3D(r, newTheta, z)
  def withRho(newRho: Double) = Vector3D(newRho, theta, phi)
  def withPhi(newPhi: Angle) = Vector3D(rho, theta, newPhi)
  def +(v: Vector3D) = Vector3D(x + v.x, y + v.y, z + v.z)
  def -(v: Vector3D) = Vector3D(x - v.x, y - v.y, z - v.z)
  def unary_- = Vector3D(-x, -y, -z, r, rho, 180.degrees + theta, 180.degrees + phi)
  def dot(v: Vector3D) = x * v.x + y * v.y + z * v.z
  def *(s: Double) =
    if (s >= 0) Vector3D(x * s, y * s, z * s, r * s, rho * s, theta, phi)
    else Vector3D(x * s, y * s, z * s, -r * s, -rho * s, 180.degrees + theta, 180.degrees + phi)
  def /(s: Double) = this * (1 / s)
  def cross(v: Vector3D) =
    Vector3D(
        y * v.z - z * v.y,
        z * v.x - x * v.z,
        x * v.y - y * v.x)
  def to2 = Vector2D(x, y, r, theta)
}

object Vector3D {
  /**
   * Returns an instance of the {@link Vector3D} class from the given
   * rectangular coordinates.
   */
  def apply(x: Double, y: Double, z: Double): Vector3D = {
    val r = Math.sqrt(x * x + y * y)
    Vector3D(x, y, z, r, Math.sqrt(r * r + z * z), Angle.atan2(y, x), Angle.atan2(r, z))
  }
  /**
   * Returns an instance of the {@link Vector3D} class from the given
   * cylindrical coordinates.
   */
  def apply(r: Double, theta: Angle, z: Double): Vector3D =
    Vector3D(r * theta.cos, r * theta.sin, z)
  /**
   * Returns an instance of the {@link Vector3D} class from the given
   * spherical coordinates.
   */
  def apply(rho: Double, theta: Angle, phi: Angle): Vector3D = {
    val rsp = rho * phi.sin
    Vector3D(rsp * theta.cos, rsp * theta.sin, rho * phi.cos)
  }
}

case class DoubleWithVectorOps(s: Double) extends AnyVal {
  def *(v: Vector2D) = v * s
  def *(v: Vector3D) = v * s
}

case class BoundsRect(p1: Vector2D, p2: Vector2D) {
  def contains(p: Vector2D) = {
    between(p.x, p1.x, p2.x) && between(p.y, p1.y, p2.y)
  }
}