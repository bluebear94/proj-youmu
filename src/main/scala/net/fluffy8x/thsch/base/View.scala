package net.fluffy8x.thsch.base

import net.fluffy8x.thsch.entity._

/**
 * @author bluebear94
 */
class View extends Parent[View, EntityManager] {
  var bounds: BoundsRect
  var parent: Option[Game] = None
  var transformer2: Transformer2D
  var transformer3: Transformer3D
}