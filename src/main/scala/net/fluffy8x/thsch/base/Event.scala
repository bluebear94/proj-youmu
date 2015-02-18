package net.fluffy8x.thsch.base

/**
 * A trait for events.
 */
trait Event {
  def trigger[Params, Result](s: Script[Params, Result]) = s.event(this)
  def triggerAll(g: Game) = g.activeScripts foreach { s: Script[_, _] =>
    trigger(s)
  }
}