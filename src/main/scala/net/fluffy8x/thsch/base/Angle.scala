package net.fluffy8x.thsch.base

/**
 * A class representing an angle value.
 * The idiomatic method is to use the implicit conversion to the
 * {@link AngleBuilder} class.
 * @author Fluffy8x
 * @see AngleBuiilder
 */
case class Angle(theta: Double) extends AnyVal {
  def sin: Double = Math.sin(theta)
  def cos: Double = Math.cos(theta)
  def tan: Double = Math.tan(theta)
  def radianValue = theta
  def degreeValue = Math.toDegrees(theta)
  def +(a: Angle) = Angle(theta + a.theta)
  def -(a: Angle) = Angle(theta - a.theta)
  def unary_-(a: Angle) = Angle(-theta)
  def *(s: Double) = Angle(s * theta)
  def /(s: Double) = Angle(theta / s)
  def /?(a: Angle) = theta / a.theta
  def <(a: Angle) = theta < a.theta
  def <=(a: Angle) = theta <= a.theta
  def >(a: Angle) = theta > a.theta
  def >=(a: Angle) = theta >= a.theta
  def ~=(a: Angle) = Math.abs((a - this) /? this) < Angle.EPSILON
  override def toString = s"$theta radians (${Math.toDegrees(theta)} degrees)"
}

object Angle {
  def sin(a: Angle) = a.sin
  def cos(a: Angle) = a.cos
  def tan(a: Angle) = a.tan
  def asin(s: Double) = Angle(Math.asin(s))
  def acos(s: Double) = Angle(Math.acos(s))
  def atan(s: Double) = Angle(Math.atan(s))
  def atan2(y: Double, x: Double) = Angle(Math.atan2(y, x))
  val EPSILON = 1e-10
}

/**
 * A class used internally for conversions.
 */
case class AngleBuilder(theta: Double) extends AnyVal {
  def radians: Angle = Angle(theta)
  def degrees: Angle = Angle(Math.toRadians(theta))
  def *(a: Angle) = a * theta
}