package net.fluffy8x.thsch.base

import net.fluffy8x.thsch.entity._
import net.fluffy8x.thsch.syntax._

/**
 * @author bluebear94
 */
class View extends Parent[View, EntityManager] {
  var bounds: BoundsRect
  var parent: Option[Game] = None
  var transformer: Transformer
  var shader: Shader
  // TODO this should be called whenever views are switched
  def set() =
    if (useGL2) {
      
    } else {
      
    }
}