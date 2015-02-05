package net.fluffy8x.thsch.entity

case class CoordinateBasis(id: Byte) extends AnyVal

object CoordinateBasis {
  val Auto = CoordinateBasis(0)
  val Window = CoordinateBasis(1)
  val Frame = CoordinateBasis(2)
}