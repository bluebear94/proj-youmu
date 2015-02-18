package net.fluffy8x.thsch.replay

import scala.collection.mutable.BitSet

/**
 * A <code>KeyStream</code> is a trait that can read keypresses either
 * from the keyboard or from a replay file.
 * @author Fluffy8x
 */
trait KeyStream {
  protected val keypresses: BitSet = new BitSet
  def readKeyPresses(): Unit
  def isKeyPressed(k: Int) = keypresses contains k
}