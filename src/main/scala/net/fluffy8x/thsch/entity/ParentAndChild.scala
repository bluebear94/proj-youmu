package net.fluffy8x.thsch.entity

import scala.collection.mutable.Set

// fix by m-z from StackOverflow
// <http://stackoverflow.com/questions/28332220/scala-type-error-for-cyclically-referenced-traits/28332724>

trait Parent[P <: Parent[P, C], C <: Child[C, P]] { this: P =>
  protected val children: scala.collection.mutable.Set[C] = Set.empty[C]
  /**
   * Adds <code>c</code> to the list of this instance's children
   * and sets the child's parent as this instance.
   */
  def register(c: C) = {
    children += c
    c.parent = this
  }
  /**
   * Calls {@link Entity}<code>.delete</code> on all children that are
   * instances of {@link Entity}.
   */
  def deleteChildren(): Unit = children foreach {
    case c: Entity => c.delete()
  }
}

trait Child[C <: Child[C, P], P <: Parent[P, C]] { this: C =>
  protected var _parent: P = _
  def parent = _parent
  def parent_=(p: P) = _parent = p
}