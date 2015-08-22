package net.fluffy8x.thsch.entity

@deprecated("0.2", "This was a stupid idea.")
/**
 * Represents an object that cannot be ticked (as there is nothing
 * to tick them).
 * e. g.: text objects
 * @author Fluffy8x
 */
trait Untickable extends Entity {
  final override def tick() {
    throw new RuntimeException("This entity cannot be ticked.")
  }
}