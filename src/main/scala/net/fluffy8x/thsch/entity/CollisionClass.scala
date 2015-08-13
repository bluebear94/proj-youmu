package net.fluffy8x.thsch.entity

/**
 * @author Fluffy8x
 */
case class CollisionClass(id: Int) extends AnyVal {
  
}

object CollisionClass {
  val Player = CollisionClass(0)
  val PlayerShot = CollisionClass(1)
  val Enemy = CollisionClass(2)
  val EnemyShot = CollisionClass(3)
}