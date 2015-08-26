package net.fluffy8x.thsch.base

import net.fluffy8x.thsch.entity._

class GameWindow(
  val width: Int = 640, val height: Int = 480,
  val stageX: Int = 32, val stageY: Int = 32,
  val stageWidth: Int = 384, val stageHeight: Int = 448
) extends Parent[GameWindow, Game] {
  def deletionBounds = BoundsRect(
    Vector2D(-64, -64),
    Vector2D(width + 64, height + 64)
  )
  def pixelToGLMat =
    Matrix.translate(-1.0f, -1.0f, 0) *
    Matrix.scale(2.0f / width, 2.0f / height, 1)
}