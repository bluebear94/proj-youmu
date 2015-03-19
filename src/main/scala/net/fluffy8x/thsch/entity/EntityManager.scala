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
  extends Child[EntityManager, Game] with Parent[EntityManager, Renderable] {
  // var player: OneOrMore[Player]
  // var enemy: Set[Enemy]
  // var boss: OneOrMore[Boss]
  // var items: Set[Item]
  // var playerShots: Set[PlayerShot]
  // var enemyShots: Set[EnemyShot]
  var renderables: TreeMap[Double, Set[Renderable]] = TreeMap.empty
  def tick() = {
    renderables foreach {
      case (renderPriority, objs) => EntityManager.removeAllDeleted(objs)
    }
  }
}
object EntityManager {
  def tickOn(things: Iterable[Entity]) =
    things.foreach(_.tick())
  def removeAllDeleted(things: Set[_ <: Entity]) =
    things.retain(!_.isDeleted)
}