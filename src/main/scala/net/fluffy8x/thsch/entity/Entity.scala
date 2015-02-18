package net.fluffy8x.thsch.entity

trait Entity {
  private var deleted = false
  def isDeleted = deleted
  def delete(): Unit = {
    deleted = true
    this match {
      case p: Parent[_, _] => p.deleteChildren()
    }
    onDelete()
  }
  def tick()
  protected def onDelete()
}