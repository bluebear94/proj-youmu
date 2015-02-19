package net.fluffy8x.thsch.base

import net.fluffy8x.thsch.entity._
import net.fluffy8x.thsch.replay._
import scala.collection.mutable.Set

/**
 * Represents a game.
 * This trait is equivalent to any playable script in Danmakufu.
 * @author Fluffy8x
 */
trait Game extends Script[Unit, Unit]
  with Child[Game, GameWindow]
  with Parent[Game, EntityManager] {
  var igKeyReader: InGameKeyStream
  val ogKeyReader: OutGameKeyStream
  val activeScripts: Set[Script[_, _]]
  val system = genSystem
  def beginScript[Params](s: Script[Params, _], p: Params) = {
    activeScripts += s
    s.main(p)
  }
  def initialize(p: Unit) = {
    beginScript(system, ())
  }
  def mainLoop() = {
    activeScripts.retain(!_.hasEnded) // The more you know.
  }
  def genSystem: System
}