package net.fluffy8x.thsch.entity

trait Entity {
  private var deleted = false
  def isDeleted = deleted
  def delete(): Unit = {
    deleted = true
    this match {
      case p: Parent[_, _] => p.deleteChildren()
    }
    onDelete(manager)
  }
  def tick()
  protected def onDelete(m: EntityManager)
  protected var _manager: EntityManager
  def manager = _manager
  def manager_=(m: EntityManager) = _manager = m
  protected def _register(m: EntityManager)
  def register(m: EntityManager) = {
    manager = m
    _register(m)
  }
}