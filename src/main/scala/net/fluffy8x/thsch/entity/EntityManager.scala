package net.fluffy8x.thsch.entity

import net.fluffy8x.thsch.base._
import net.fluffy8x.thsch.syntax._

/**
 * An entity manager.
 * Keeps track of every entity, separated by type
 * (i. e. players, enemies, boss scenes, items, misc.)
 */
class EntityManager extends Child[EntityManager, Game] with Parent[EntityManager, Renderable] {
  // var player: OneOrMore[Player]
  // var enemy: Set[Enemy]
  // var boss: OneOrMore[Boss]
}