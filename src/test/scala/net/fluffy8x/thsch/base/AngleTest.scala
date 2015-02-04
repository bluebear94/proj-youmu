package net.fluffy8x.thsch.base

import org.scalatest._
import net.fluffy8x.thsch.base._

class AngleTest extends FlatSpec with Matchers {
  "Angle" should "handle angles" in {
    5.degrees should equal(5.degrees)
    (1.radians + 1.radians) should equal(2.radians)
    (3.degrees - 2.degrees ~= 1.degrees) should equal(true)
    1.radians.theta should equal(1)
    90.degrees should equal((Math.PI/2).radians)
    3.degrees should be < 5.degrees
    3.degrees should not be > (5.degrees)
    3.degrees should be <= 5.degrees
    3.degrees should not be >= (5.degrees)
  }
  "Angle" should "handle trigonometry" in {
    45.degrees.sin shouldEqual (Math.sqrt(2)/2 +- 0.0001)
    Angle.cos((Math.PI/3).radians) shouldEqual(0.5 +- 0.0001)
  }
}