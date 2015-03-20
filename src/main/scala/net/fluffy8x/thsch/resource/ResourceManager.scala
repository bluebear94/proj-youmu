package net.fluffy8x.thsch.resource

import org.lwjgl.glfw._
import scala.collection.mutable.HashMap
import java.io.File

/**
 * Keeps track of images, sounds, models, etc.
 * Is an object since little would be gained otherwise.
 * @author Fluffy8x
 */
object ResourceManager {
  val IMG_SIZE_CAPACITY = 100000000 // 100M
  val images = new HashMap[File, SCHTexture]
  private var _totalImageSize = 0
  def totalImageSize = _totalImageSize
  def getImage(path: File) = {
    val aPath = path.getAbsoluteFile
    images.get(aPath) match {
      case Some(texture) => texture
      case None => {
        while (_totalImageSize > IMG_SIZE_CAPACITY) {
          val (oldestFile, oldestTexture) = images.minBy(_._2.lastUsed)
          _totalImageSize -= oldestTexture.size
          images -= oldestFile
        }
        val texture = SCHTexture.read(aPath)
        images(aPath) = texture
        _totalImageSize += texture.size
      }
    }
  }
}