package net.fluffy8x.thsch.entity

import scala.collection.immutable.IntMap
import scala.collection.mutable._
import net.fluffy8x.thsch.base.{ Vector2D, BoundsRect }

/**
 * @author Fluffy8x
 */
class CollisionStructure {
  var map: IntMap[(Set[Collidable], QuadTree)]
}

class QuadTree(val boundary: BoundsRect) {
  val maxNodesPerLeaf = 8
  var points: Set[Collidable] = Set.empty
  var isLeaf = true
  var nw: QuadTree
  var ne: QuadTree
  var sw: QuadTree
  var se: QuadTree
  def queryBoundsAll(rect: BoundsRect): Set[Collidable] = {
    if (!boundary.intersects(rect)) Set()
    else {
      val pointsInRange = pointsInBoundary(rect)
      if (isLeaf) pointsInRange
      else {
        pointsInRange |
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
      val pointsInRange = points.filter(h collides _.hitbox)
      if (isLeaf) pointsInRange
      else {
        pointsInRange |
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
      val pointsInRange = pointsInBoundary(rect).headOption
      if (isLeaf) pointsInRange
      else {
        pointsInRange orElse
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
      val pointsInRange = points.filter(h collides _.hitbox).headOption
      if (isLeaf) pointsInRange
      else {
        pointsInRange orElse
          nw.queryIntersect(h) orElse
          ne.queryIntersect(h) orElse
          sw.queryIntersect(h) orElse
          se.queryIntersect(h)
      }
    }
  }
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
  }
  def pointsInBoundary(b: BoundsRect) =
    points.filter(b contains
      _.hitbox.asInstanceOf[Circle].c)
}
