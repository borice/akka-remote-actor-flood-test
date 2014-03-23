package remote

import akka.actor.{ActorLogging, Actor}

class Receiver extends Actor with ActorLogging {
  import Protocol._

  var receivedCount = 0
  var timestamp = 0L

  override def receive = {
    case Message(i) =>
      if (receivedCount == 0)
        timestamp = System.currentTimeMillis

      receivedCount += 1

      if (receivedCount % 20000 == 0) {
        val newTimestamp = System.currentTimeMillis
        val delta = newTimestamp - timestamp
        timestamp = newTimestamp
        log.info(f"delta = $delta%,d ms, received = $receivedCount%,d")
      }

    case NoMoreMessages =>
      log.info(s"Sender signaled NoMoreMessages; Received $receivedCount messages")
  }
}
