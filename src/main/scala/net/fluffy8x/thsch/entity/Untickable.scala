package net.fluffy8x.thsch.entity

/**
 * Represents an object that cannot be ticked (as there is nothing
 * to tick them).
 * e. g.: text objects
 * @author Fluffy8x
 */
trait Untickable extends Entity {
  final def tick() {
    throw new RuntimeException("This entity cannot be ticked.")
  }
}