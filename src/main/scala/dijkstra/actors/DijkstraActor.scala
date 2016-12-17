package dijkstra.actors

import akka.actor.{Actor, ActorRef}
import dijkstra.config.RingConfig
import dijkstra.messages.{NeighbourAck, NextAnnouncement, NodeState}

abstract class DijkstraActor(id: Int, ringConfig: RingConfig) extends Actor {
  private var state = 0
  private var next: Option[ActorRef] = None

  override def receive: Receive = {
    case NodeState(neighbourState) => receiveNeighbourState(neighbourState)
    case NextAnnouncement(neighbour) => receiveNext(neighbour)
  }

  private def receiveNeighbourState(neighbourState: Int): Unit = {
    showState(state)
    state = recalculateState(state, neighbourState)
    Thread.sleep(1000)
    next.foreach(_ ! NodeState(state))
  }

  protected def showState(oldState: Int): Unit

  protected def recalculateState(oldState: Int, neighbourState: Int): Int

  private def receiveNext(neighbour: ActorRef): Unit = {
    next = Some(neighbour)
    sender() ! NeighbourAck
  }
}
