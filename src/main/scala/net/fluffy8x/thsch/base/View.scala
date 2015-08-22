package net.fluffy8x.thsch.base

import net.fluffy8x.thsch.entity._
import net.fluffy8x.thsch.syntax._

/**
 * @author bluebear94
 */
class View(
    var bounds: BoundsRect,
    var transformer: Transformer,
    var shader: Option[Shader],
    var parent: Option[Game] = None
) extends Parent[View, EntityManager] {
  // TODO this should be called whenever views are switched
  def set() =
    if (useGL3) {
      
    } else {
      
    }
}