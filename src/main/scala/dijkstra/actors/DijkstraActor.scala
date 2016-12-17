package dijkstra.actors

import java.util.Date

import akka.actor.{Actor, ActorRef}
import dijkstra.config.RingConfig
import dijkstra.messages.{NeighbourAck, NextAnnouncement, NodeDisturbance, NodeState}

import scala.util.Random

abstract class DijkstraActor(id: Int, config: RingConfig) extends Actor {
  protected var state = 0
  protected var next: Option[ActorRef] = None
  protected val random: Random = new Random(new Date().getTime - id)

  override def receive: Receive = {
    case NodeState(neighbourState) => onStateReceive(neighbourState)
    case NextAnnouncement(neighbour) => receiveNext(neighbour)
    case NodeDisturbance => state = random.nextInt(config.K)
  }

  protected def onStateReceive(neighbourState: Int): Unit

  private def receiveNext(neighbour: ActorRef): Unit = {
    next = Some(neighbour)
    sender() ! NeighbourAck
  }
}
