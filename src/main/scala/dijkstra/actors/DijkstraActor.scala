package dijkstra.actors

import java.util.Date

import akka.actor.{Actor, ActorRef}
import dijkstra.config.RingConfig
import dijkstra.messages.{NeighbourAck, NextAnnouncement, NodeDisturbance, NodeState}

import scala.util.Random

abstract class DijkstraActor(id: Int, config: RingConfig) extends Actor {
  private var state = 0
  private var next: Option[ActorRef] = None
  private val random: Random = new Random(new Date().getTime - id)

  override def receive: Receive = {
    case NodeState(neighbourState) => receiveNeighbourState(neighbourState)
    case NextAnnouncement(neighbour) => receiveNext(neighbour)
    case NodeDisturbance => state = random.nextInt(config.K)
  }

  private def receiveNeighbourState(neighbourState: Int): Unit = {
    state = recalculateState(state, neighbourState)
    showState(state, neighbourState)
    Thread.sleep(500)
    next.foreach(_ ! NodeState(state))
  }

  protected def showState(state: Int, neighbourState: Int): Unit

  protected def recalculateState(oldState: Int, neighbourState: Int): Int

  private def receiveNext(neighbour: ActorRef): Unit = {
    next = Some(neighbour)
    sender() ! NeighbourAck
  }
}
