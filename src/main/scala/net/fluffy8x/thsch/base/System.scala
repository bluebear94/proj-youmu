package net.fluffy8x.thsch.base

/**
 * A system script.
 * This type of script controls the HUD.
 */
trait System extends Script[Unit, Unit] {
  def pauseMenu(): MenuResult
  def endMenu(): MenuResult
}

trait MenuResult
case object Resume extends MenuResult
case object Stop extends MenuResult
case object Restart extends MenuResult