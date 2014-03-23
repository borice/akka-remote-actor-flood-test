package remote

import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.slf4j.Logging

object Main extends App with Logging {
  if (args.length < 1)
    throw new RuntimeException("Missing cmd line argument")

  args.head match {
    case "receiver" =>
      val systemA = ActorSystem("systemA", ConfigFactory.load("receiver"))
      val receiver = systemA.actorOf(Props[Receiver], "receiver")
      logger.info(s"Created receiver at ${receiver.path}")

    case "sender" =>
      val receiverPath = "akka.tcp://systemA@localhost:2553/user/receiver"
      val systemB = ActorSystem("systemB", ConfigFactory.load("sender"))
      val messageCount = args(1).toInt
      val sender = systemB.actorOf(Props(classOf[Sender], receiverPath, messageCount), "sender")
      logger.info(s"Created sender at ${sender.path}")

    case _ => throw new RuntimeException("Invalid command line args specified")
  }
}
