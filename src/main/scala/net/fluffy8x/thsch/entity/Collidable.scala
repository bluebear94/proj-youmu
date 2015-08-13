package net.fluffy8x.thsch.entity

import net.fluffy8x.thsch.base._

/**
 * Defines an entity with a hitbox.
 */
trait Collidable extends Entity {
  def hitbox: Hitbox
  def isInvincible: Boolean
  def collisionClass: CollisionClass
  def _register(m: EntityManager): Unit = {
    super._register(m)
    m.collidables += this
  }
  def onDelete(m: EntityManager) = {
    super.onDelete(m)
    m.collidables -= this
  }
}

/**
 * Defines an entity with a set position.
 * Most instances of {@link Collidable} will be instances
 * of {@link Movable} as well.
 */
trait Movable extends Collidable {
  var s: Vector3D
  var v: Vector3D
  var a: Vector3D
  var j: Vector3D
  var omega: Angle
  var alpha: Angle
  def update(): Unit = {
    s += v
    v += a
    a += j
    v = v.withTheta(v.theta + omega)
    omega += alpha
  }
  def relativeHitbox: Hitbox
  def hitbox = relativeHitbox.offset(s.to2)
}

trait WithHealth extends Collidable {
  var health: Double
}