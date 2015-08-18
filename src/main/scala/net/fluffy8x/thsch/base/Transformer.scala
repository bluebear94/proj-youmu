package net.fluffy8x.thsch.base

/**
 * @author bluebear94
 */
trait Transformer2D {
  def transform(p: Vector2D): Vector2D
}

trait Transformer3D {
  def transform(p: Vector3D): Vector2D
}

trait Shader {
  def src: String
}