package net.fluffy8x.thsch.replay

import scala.collection.mutable.BitSet
import net.fluffy8x.thsch.base.Point2D

/**
 * A <code>KeyStream</code> is a trait that can read keypresses either
 * from the keyboard or from a replay file.
 * @author Fluffy8x
 */
sealed trait KeyStream {
  protected val keypresses: BitSet = new BitSet
  protected var _mouse: Point2D = Point2D(0, 0)
  def readKeyPresses(): Unit
  def isKeyPressed(k: Int) = keypresses contains k
  def mouse = _mouse
}

/**
 * A subclass of {@link KeyStream} that reads keypresses relevant
 * to games.
 * @author Fluffy8x
 */
trait InGameKeyStream extends KeyStream {
  
}

trait OutGameKeyStream extends KeyStream {
  
}