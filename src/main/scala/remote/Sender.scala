package remote

import akka.actor._
import scala.concurrent.duration._

object Sender {
  private case object Start
}

class Sender(path: String, messageCount: Int) extends Actor with ActorLogging {
  import Sender._
  import Protocol._

  sendIdentifyRequest()

  def sendIdentifyRequest() = {
    context.actorSelection(path) ! Identify(path)
    import context.dispatcher
    context.system.scheduler.scheduleOnce(3.seconds, self, ReceiveTimeout)
  }

  override def receive = identifying

  def identifying: Receive = {
    case ActorIdentity(`path`, Some(actor)) =>
      log.info(s"Found receiver at ${actor.path}")
//      context.watch(actor)
      context.become(active(actor))
      self ! Start

    case ActorIdentity(`path`, None) => log.warning(s"Remote actor not available: $path")
    case ReceiveTimeout              => sendIdentifyRequest()
    case _                           => log.warning("Not ready to receive yet")
  }

  def active(receiver: ActorRef): Receive = {
    case Start =>
      log.info(s"Sending $messageCount messages")
      for (i <- 1 to messageCount) {
//        if (i % 100000 == 0)
//          Thread.sleep(5000)
        receiver ! Message(i)
      }
      receiver ! NoMoreMessages
      log.info("Done sending")
//      context.unwatch(receiver)
      context.stop(self)

    case Terminated(`receiver`) =>
      log.warning("Receiver terminated")
      sendIdentifyRequest()
      context.become(identifying)

    case ReceiveTimeout => // ignore
  }
}
