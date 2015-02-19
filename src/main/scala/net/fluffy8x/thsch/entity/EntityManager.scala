package net.fluffy8x.thsch.entity

import net.fluffy8x.thsch.base._
import net.fluffy8x.thsch.syntax._
import scala.collection.mutable.Set

/**
 * An entity manager.
 * Keeps track of every entity, separated by type
 * (i. e. players, enemies, boss scenes, items, misc.)
 */
class EntityManager extends Child[EntityManager, Game] with Parent[EntityManager, Renderable] {
  // var player: OneOrMore[Player]
  // var enemy: Set[Enemy]
  // var boss: OneOrMore[Boss]
  // var items: Set[Item]
  val miscPrimitives: Set[Primitive2D] = Set.empty
  def tick() = {
    tickOn(miscPrimitives)
    removeAllDeleted(miscPrimitives)
  }
  def tickOn(things: Iterable[Entity]) =
    things.foreach(_.tick())
  def removeAllDeleted(things: Set[_ <: Entity]) =
    things.retain(!_.isDeleted)
}