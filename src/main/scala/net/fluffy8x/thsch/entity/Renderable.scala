package net.fluffy8x.thsch.entity

import net.fluffy8x.thsch.base._
import net.fluffy8x.thsch.base.Point3D

trait Renderable extends Entity with Child[Renderable, EntityManager] {
  var position: Point3D
  def render() {
    if (isVisible) _render()
  }
  protected def _render(): Unit
  var isVisible = false
  def basis: CoordinateBasis
  def renderPriority: Double
  def upperLeft = basis match {
    case CoordinateBasis.Window => Point2D(0, 0)
    case CoordinateBasis.Frame => {
      val window = parent.parent.parent
      Point2D(window.stageX, window.stageY)
    }
    case CoordinateBasis.Auto => {
      if (renderPriority >= 0.2 && renderPriority <= 0.8) {
        val window = parent.parent.parent
        Point2D(window.stageX, window.stageY)
      } else Point2D(0, 0)
    }
  }
}
