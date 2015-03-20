package net.fluffy8x.thsch.entity

import net.fluffy8x.thsch.base.{ Point2D, Vector2D, Point3D, BoundsRect, Color }
import net.fluffy8x.thsch.resource._

trait Shot extends Movable with Renderable {
  def _register(m: EntityManager): Unit = {
    super._register(m)
    m.enemyShots += this
  }
}

trait PlayerShot extends Shot {
  def _register(m: EntityManager): Unit = {
    super._register(m)
    m.enemyShots -= this
    m.playerShots += this
  }
}

case class ShotData(
  texture: SCHTexture,
  spriteBounds: BoundsRect,
  destBounds: BoundsRect,
  hitboxIfPresent: Option[Hitbox] = None,
  blendMode: BlendMode = BlendMode.Alpha,
  fog: Color = Color(0xFFFFFFFF),
  fixedAngle: Boolean = false) {
  def createSprite: Sprite2D =
    new Sprite2D(texture, spriteBounds, destBounds, !fixedAngle, blendMode)
  lazy val hitbox: Hitbox = hitboxIfPresent getOrElse
    Circle(Point2D(0, 0), 0.8 * (destBounds.p1 - destBounds.p2).r)
}