package net.fluffy8x.thsch.entity

@deprecated("Use View instead", "0.2")
case class CoordinateBasis(id: Byte) extends AnyVal

@deprecated("Use View instead", "0.2")
object CoordinateBasis {
  val Auto = CoordinateBasis(0)
  val Window = CoordinateBasis(1)
  val Frame = CoordinateBasis(2)
}