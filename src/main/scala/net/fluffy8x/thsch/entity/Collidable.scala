package net.fluffy8x.thsch.entity

import net.fluffy8x.thsch.base._

/**
 * Defines an entity with a hitbox.
 */
trait Collidable extends Entity {
  def hitbox: Hitbox
  def isInvincible: Boolean
}

/**
 * Defines an entity with a set position.
 * Most instances of {@link Collidable} will be instances
 * of {@link Movable} as well.
 */
trait Movable extends Collidable {
  var s: Point2D
  var v: Vector2D
  var a: Vector2D
  var j: Vector2D
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
  def hitbox = relativeHitbox.offset(s - Point2D(0, 0))
}

trait WithHealth extends Collidable {
  var health: Double
}