package net.fluffy8x.thsch.entity

import net.fluffy8x.thsch.base._
import net.fluffy8x.thsch.base.Point3D

/**
 * Describes an entity that can be rendered.
 */
trait Renderable extends Entity with Child[Renderable, EntityManager] {
  var position: Point3D
  var angle: Angle
  def render() {
    if (isVisible) _render()
  }
  protected def _render(): Unit
  def tick() {
    super.tick()
    render()
  }
  var isVisible = false
  def basis: CoordinateBasis
  def renderPriority: Double
  /**
   * Returns the upper left corner based on the current {@link CoordinateBasis}
   * and render priority.
   */
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
