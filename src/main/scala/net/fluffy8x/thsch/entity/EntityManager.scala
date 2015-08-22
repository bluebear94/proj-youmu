package net.fluffy8x.thsch.entity

import net.fluffy8x.thsch.base._
import net.fluffy8x.thsch.syntax._
import scala.collection.immutable.TreeMap // Y U NO HAVE MUTABLE TREEMAP?!!
import scala.collection.mutable.Set

/**
 * An entity manager.
 * Keeps track of every entity, separated by type
 * (i. e. players, enemies, boss scenes, items, misc.)
 * @author Fluffy8x
 */
class EntityManager
  extends Child[EntityManager, View] {
  // var player: OneOrMore[Player]
  // var enemy: Set[Enemy]
  // var boss: OneOrMore[Boss]
  // var items: Set[Item]
  var collidables = new CollisionStructure
  var renderables: TreeMap[Double, Set[Renderable]] = TreeMap.empty
  var entities: Set[Entity] = Set.empty
  def tick() = {
    renderables foreach {
      case (renderPriority, objs) => EntityManager.removeAllDeleted(objs)
    }
    EntityManager.tickOn(entities)
    EntityManager.removeAllDeleted(entities)
  }
  def renderAll() = {
    for ((priority, objects) <- renderables)
      for (e <- objects)
        e.render()
  }
}
object EntityManager {
  def tickOn(things: Iterable[Entity]) =
    things.foreach(_.tick())
  def removeAllDeleted(things: Set[_ <: Entity]) =
    things.retain(!_.isDeleted)
}