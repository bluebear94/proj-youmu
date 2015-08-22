package net.fluffy8x.thsch.entity

import scala.collection.immutable.IntMap
import scala.collection.mutable._
import net.fluffy8x.thsch.base.{ Vector2D, BoundsRect }

/**
 * @author Fluffy8x
 */
class CollisionStructure extends Iterable[Collidable] {
  var map: IntMap[(Set[Collidable], QuadTree)] = IntMap.empty
  def getByCC(c: CollisionClass) = map(c.id)
  def +=(c: Collidable) = c.hitbox match {
    case l: Line => getByCC(c.collisionClass)._1 += c
    case m: Circle => getByCC(c.collisionClass)._2 += c
  }
  def -=(c: Collidable) = c.hitbox match {
    case l: Line => getByCC(c.collisionClass)._1 -= c
    case m: Circle => getByCC(c.collisionClass)._2 -= c
  }
  def iterator = map.iterator.map {
    case (_, (lines, circles)) =>
      lines.iterator ++ circles.iterator
  }.reduce(_ ++ _)
}

class QuadTree(val boundary: BoundsRect)
    extends Iterable[Collidable] {
  val maxNodesPerLeaf = 8
  val leafCompactionThreshhold = maxNodesPerLeaf >> 1
  var points: Set[Collidable] = Set.empty
  var isLeaf = true
  var nw: QuadTree = null
  var ne: QuadTree = null
  var sw: QuadTree = null
  var se: QuadTree = null
  def queryBoundsAll(rect: BoundsRect): Set[Collidable] = {
    if (!boundary.intersects(rect)) Set()
    else {
      if (isLeaf) pointsInBoundary(rect)
      else {
        nw.queryBoundsAll(rect) |
          ne.queryBoundsAll(rect) |
          sw.queryBoundsAll(rect) |
          se.queryBoundsAll(rect)
      }
    }
  }
  def queryIntersectAll(h: Hitbox): Set[Collidable] = {
    if (!boundary.intersects(h)) Set()
    else {
      if (isLeaf) points.filter(h collides _.hitbox)
      else {
        nw.queryIntersectAll(h) |
          ne.queryIntersectAll(h) |
          sw.queryIntersectAll(h) |
          se.queryIntersectAll(h)
      }
    }
  }
  def queryBounds(rect: BoundsRect):  Option[Collidable] = {
    if (!boundary.intersects(rect)) None
    else {
      if (isLeaf) pointsInBoundary(rect).headOption
      else {
        nw.queryBounds(rect) orElse
          ne.queryBounds(rect) orElse
          sw.queryBounds(rect) orElse
          se.queryBounds(rect)
      }
    }
  }
  def queryIntersect(h: Hitbox): Option[Collidable] = {
    if (!boundary.intersects(h)) None
    else {
      if (isLeaf) points.filter(h collides _.hitbox).headOption
      else {
        nw.queryIntersect(h) orElse
          ne.queryIntersect(h) orElse
          sw.queryIntersect(h) orElse
          se.queryIntersect(h)
      }
    }
  }
  def +=(p: Collidable) = insert(p)
  def insert(p: Collidable): Unit = p.hitbox match {
    case Circle(center, radius) ⇒ {
      if (!boundary.contains(center))
        throw new IndexOutOfBoundsException(
          s"$boundary does not contain $center"
        )
      if (isLeaf && points.size < maxNodesPerLeaf) {
        points += p
      } else {
        if (isLeaf) subdivide()
        if (nw.boundary contains center) nw.insert(p)
        else if (ne.boundary contains center) ne.insert(p)
        else if (sw.boundary contains center) sw.insert(p)
        else if (se.boundary contains center) se.insert(p)
        else throw new RuntimeException("Quadtree insertion buggy")
      }
    }
    case _ ⇒
      throw new IllegalArgumentException(
        "quadtrees are circle-only clubs"
      )
  }
  def subdivide() = {
    createNewRectangles()
    relocatePoints()
  }
  def createNewRectangles() = {
    isLeaf = false
    val nwp = nw.boundary.p1
    val sep = nw.boundary.p2
    val mid = (nwp + sep) / 2
    nw = new QuadTree(BoundsRect(nwp, mid))
    se = new QuadTree(BoundsRect(mid, sep))
    val ncp = Vector2D(mid.x, nwp.y)
    val ecp = Vector2D(sep.x, mid.y)
    val wcp = Vector2D(nwp.x, mid.y)
    val scp = Vector2D(mid.x, sep.y)
    ne = new QuadTree(BoundsRect(ncp, ecp))
    sw = new QuadTree(BoundsRect(wcp, scp))
  }
  def relocatePoints() = {
    nw.points = pointsInBoundary(nw.boundary)
    ne.points = pointsInBoundary(ne.boundary)
    sw.points = pointsInBoundary(sw.boundary)
    se.points = pointsInBoundary(se.boundary)
    points = null
  }
  def pointsInBoundary(b: BoundsRect) =
    points.filter(b contains
      _.hitbox.asInstanceOf[Circle].c)
  def -=(p: Collidable) = remove(p)
  def remove(p: Collidable): Boolean = p.hitbox match {
    case Circle(center, radius) => {
      if (!boundary.contains(center)) false
      else if (isLeaf) {
        if (points contains p) {
          points -= p
          true
        } else false
      } else {
        if (!nw.remove(p) || ne.remove(p) ||
            sw.remove(p) || se.remove(p))
          false
        else if (canCompact) {
          compact()
          true
        } else true
      }
    }
    case _ =>
      throw new IllegalArgumentException(
        "quadtrees are circle-only clubs"
      )
  }
  def canCompact = nw.isLeaf && ne.isLeaf &&
    sw.isLeaf && se.isLeaf &&
    nw.points.size + ne.points.size +
    sw.points.size + se.points.size < leafCompactionThreshhold
  def compact() = {
    isLeaf = false
    points = nw.points | ne.points | sw.points | se.points
    nw = null // ごめんなさい
    ne = null
    sw = null
    se = null
  }
  def iterator =
    if (isLeaf) points.iterator
    else nw.iterator ++ ne.iterator ++ sw.iterator ++ se.iterator
}
