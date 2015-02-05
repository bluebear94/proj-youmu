package net.fluffy8x.thsch.base

import net.fluffy8x.thsch.entity._

trait Game extends Script[Unit, Unit] with Child[Game, GameWindow] with Parent[Game, EntityManager] {

}