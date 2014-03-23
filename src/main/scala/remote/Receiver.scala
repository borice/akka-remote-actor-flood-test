package remote

import akka.actor.{ActorLogging, Actor}
import scala.concurrent.duration._

object Receiver {
  private case object ReportStatus
}

class Receiver extends Actor with ActorLogging {
  import Receiver._
  import Protocol._
  import context.dispatcher

  val statusReportInterval = 5.seconds
  //val reportTask = context.system.scheduler.schedule(statusReportInterval, statusReportInterval, self, ReportStatus)

  var receivedCount = 0

  //override def postStop() = reportTask.cancel()

  override def receive = {
    case Message(i) =>
      receivedCount += 1
      if (i % 20000 == 0) reportStatus()

    case NoMoreMessages =>
      log.info("Sender signaled NoMoreMessages")
      reportStatus()

    case ReportStatus => reportStatus()
  }

  def reportStatus() = log.info(s"[STATUS] receivedCount=$receivedCount")
}
