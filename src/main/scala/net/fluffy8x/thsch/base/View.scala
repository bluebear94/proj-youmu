package net.fluffy8x.thsch.base

import net.fluffy8x.thsch.entity._
import net.fluffy8x.thsch.syntax._

/**
 * @author bluebear94
 */
class View extends Parent[View, EntityManager] {
  var bounds: BoundsRect
  var parent: Option[Game] = None
  var transformer2: Transformer2D
  var transformer3: Transformer3D
  var shader: Shader
  // TODO this should be called whenever views are switched
  def set() =
    if (useGL2) {
      
    } else {
      
    }
}