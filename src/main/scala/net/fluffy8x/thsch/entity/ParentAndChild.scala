package net.fluffy8x.thsch.entity

import scala.collection.mutable.Set

// fix by m-z from StackOverflow
// <http://stackoverflow.com/questions/28332220/scala-type-error-for-cyclically-referenced-traits/28332724>

trait Parent[P <: Parent[P, C], C <: Child[C, P]] { this: P =>
  protected val children: scala.collection.mutable.Set[C]
  def register(c: C) = {
    children += c
    c.parent = this
  }
  def deleteChildren(): Unit = children foreach {
    case c: Entity => c.delete()
  }
}

trait Child[C <: Child[C, P], P <: Parent[P, C]] { this: C =>
  protected var _parent: P
  def parent = _parent
  def parent_=(p: P) = _parent = p
}