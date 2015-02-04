package net.fluffy8x.thsch.base

import continuations.Coroutines

/**
 * A trait for scripts.
 * The traditional script that is the same, without parameters or return
 * values, would be described as <code>Script[Unit, Unit]</code>.
 * @author Fluffy8x
 */
trait Script[Params, Result] {
  private var _ended = false
  def hasEnded = _ended
  def end() = _ended = true
  def initialize(p: Params)
  def mainLoop()
  def event(ev: Event)
  def genres(): Result // not called finalize as to avoid conflict
                       // with Java's interpretation
  def main(p: Params): Result = {
    initialize(p)
    while (!hasEnded) Coroutines.coroutine {
      mainLoop()
      Coroutines.yld
    }
    genres()
  }
}