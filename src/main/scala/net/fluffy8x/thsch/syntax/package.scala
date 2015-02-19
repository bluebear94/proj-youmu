package net.fluffy8x.thsch

import continuations._

package object syntax {
  /**
   * Given an <code>Iterable[(A) => Option[A]]</code>,
   * gives <code>Some(value)</code> if each one returns a <code>Some</code>;
   * otherwise a <code>None</code>.
   */
  def allOrNothing[A](hoops: Iterable[(A) => Option[A]])(seed: A): Option[A] = {
    var res = seed
    for (hoop <- hoops) {
      hoop(res) match {
        case Some(r) => res = r
        case None => return None
      }
    }
    Some(res)
  }
  /**
   * Acts like <code>allOrNothing</code>, but if one function returns a
   * <code>None</code>, then control is passed back to the previous function
   * if present.
   */
  def zippedSequence[A](hoops: Seq[(A) => Option[A]])(seed: A): Option[A] = {
    var it = hoops
    var res = seed
    var prevs: List[(A) => Option[A]] = Nil
    while (!it.isEmpty) {
      val hoop = it.head
      it = it.tail
      hoop(res) match {
        case Some(r) => {
          res = r
          prevs = hoop :: prevs
        }
        case None => prevs match {
          case Nil => return None
          case prev :: others => {
            it = prev +: it
            prevs = others
          }
        }
      }
    }
    Some(res)
  }
  def coroutine(u: => Unit) = Coroutines.coroutine(u)
  def yld = Coroutines.yld
  implicit def singleToOOM[A](a: A) = OneOrMore(a)
  implicit def listToOOM[A](as: List[A]) = OneOrMore(as)
  implicit def oomToSingle[A](oom: OneOrMore[A]) = oom.underlying.head
  implicit def oomToList[A](oom: OneOrMore[A]) = oom.underlying
  def between(a: Double, b: Double, c: Double) = {
    a >= (b min c) && a < (b max c)
  }  
}