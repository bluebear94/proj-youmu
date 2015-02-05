package net.fluffy8x.thsch.entity

import net.fluffy8x.thsch.base._

trait Hitbox {
  def collides(that: Hitbox): Boolean
}

case class Line(a: Point2D, b: Point2D) extends Hitbox {
  def collides(that: Hitbox) = that match {
    case Line(c, d) => {
      val r = b - a
      val s = d - c
      val rs = r cross2D s
      val cma = c - a
      val t = (cma cross2D s) / rs
      val u = (cma cross2D r) / rs
      t >= 0 && t <= 1 && u >= 0 && u <= 1
    }
    case Circle(c, r) => {
      val ab = b - a
      val ac = c - a
      val ad = ac proj ab
      val cd = ac - ad
      (cd dot cd) <= (r * r)
    }
    case p: Polygon => p collides this
  }
}

case class Circle(c: Point2D, r: Double) extends Hitbox {
  def collides(that: Hitbox) = that match {
    case Circle(c2, r2) => {
      (c2 - c).r2 < ((r + r2) * (r + r2))
    }
    case x => x collides this
  }
}

case class Polygon(vertices: List[Point2D], closed: Boolean = true)
    extends Hitbox {
  val trueVertices = if (closed) (vertices.last :: vertices) else vertices
  val edges = trueVertices.sliding(1).map {
    case List(a, b) => Line(a, b)
  }.toList
  def collides(that: Hitbox) = edges.exists(_ collides that)
}