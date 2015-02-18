package net.fluffy8x.thsch.syntax

/**
 * Represents something of which there can be multiple instances, but
 * can usually assumed to have one.
 */
case class OneOrMore[+A](underlying: List[A]) {
  require(!underlying.isEmpty)
}
object OneOrMore {
  def apply[A](a: A): OneOrMore[A] = OneOrMore(List(a))
}