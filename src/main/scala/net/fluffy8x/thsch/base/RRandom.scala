package net.fluffy8x.thsch.base

import scala.util.Random

/**
 * A wrapper for the Random object.
 * This object is recommended for ensuring that replays synchronize
 * properly.
 * @author Fluffy8x
 */
object RRandom {
  val underlying: Random = new Random
}