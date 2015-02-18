package net.fluffy8x.thsch.base

import org.lwjgl.glfw._
import scala.collection.mutable.HashMap

/**
 * Keeps track of images, sounds, models, etc.
 * Is an object since little would be gained otherwise.
 * @author Fluffy8x
 */
object ResourceManager {
  val images = new HashMap[String, GLFWimage]
}