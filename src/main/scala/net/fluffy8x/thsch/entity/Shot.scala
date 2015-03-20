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
  hitboxBounds: BoundsRect,
  blendMode: BlendMode = BlendMode.Alpha,
  fog: Color = Color(0xFFFFFFFF),
  fixedAngle: Boolean = false) {
  def createPrimitive: Primitive2D = {
    val obj = Primitive2D.sprite(texture, spriteBounds, hitboxBounds)
    obj.rotatable = !fixedAngle
    obj
  }
}