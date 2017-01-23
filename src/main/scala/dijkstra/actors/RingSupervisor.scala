package dijkstra.actors

import java.util.Date

import akka.actor.{Actor, ActorRef, Props}
import dijkstra.config.RingConfig
import dijkstra.messages._
import misra.demo.Writer

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.Random

class RingSupervisor(config: RingConfig) extends Actor {
  private var dijkstraActors: List[ActorRef] = Nil
  private var numberOfUninitializedNodes = config.numberOfNodes
  private val random: Random = new Random(new Date().getTime)

  override def receive: Receive = {
    case Start => init()
    case NeighbourAck => receiveNeighbourAck()
  }

  private def init(): Unit = {
    val logger = context.actorOf(Props[StateLogger])

    val distinguishedConsumer = context.actorOf(Props(classOf[PrivilegeConsumer], 1, logger))
    val distinguishedDijkstraActor = context.actorOf(Props(classOf[DistinguishedActor], distinguishedConsumer, config))

    val ordinaryDijkstraActors = Range.inclusive(2, config.numberOfNodes).map(consumerId => {
      val consumer = context.actorOf(Props(classOf[PrivilegeConsumer], consumerId, logger))
      context.actorOf(Props(classOf[OrdinaryActor], consumer))
    })

    val lastOrdinaryNode = ordinaryDijkstraActors.foldLeft(distinguishedDijkstraActor){ (previous, current) => {
      previous ! NextAnnouncement(current); current
    }}
    lastOrdinaryNode ! NextAnnouncement(distinguishedDijkstraActor)
    dijkstraActors = distinguishedDijkstraActor :: ordinaryDijkstraActors.toList
  }

  private def receiveNeighbourAck(): Unit = {
    numberOfUninitializedNodes -= 1
    if(numberOfUninitializedNodes == 0) {
      Writer.clearScreen()
      dijkstraActors.head ! NodeState(0)
      interactWithUser()
    }
  }

  @tailrec
  private def interactWithUser(): Unit = {
    StdIn.readLine() match {
      case "q" => context.system.terminate()
      case _ => dijkstraActors.foreach(_ ! ForcedStateChange(random.nextInt(config.K))); interactWithUser()
    }
  }
}
