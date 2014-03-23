package remote

object Protocol {
  case class Message(id: Int)
  case object NoMoreMessages
}
