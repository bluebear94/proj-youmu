package net.fluffy8x.thsch.entity

import net.fluffy8x.thsch.base.{ Vector2D, BoundsRect, Color }
import net.fluffy8x.thsch.resource._

trait Shot extends Renderable with Movable {
  def collisionClass = CollisionClass.EnemyShot
}

trait PlayerShot extends Shot {
  override def collisionClass = CollisionClass.PlayerShot
}

case class ShotData(
  texture: SCHTexture,
  spriteBounds: BoundsRect,
  destBounds: BoundsRect,
  hitboxIfPresent: Option[Hitbox] = None,
  blendMode: BlendMode = BlendMode.Alpha,
  fog: Color = Color(0xFFFFFFFF),
  fixedAngle: Boolean = false) {
  //def createSprite: Sprite2D =
  //  new Sprite2D(texture, spriteBounds, destBounds, !fixedAngle, blendMode)
  lazy val hitbox: Hitbox = hitboxIfPresent getOrElse
    Circle(Vector2D(0, 0), 0.8 * (destBounds.p1 - destBounds.p2).r)
}