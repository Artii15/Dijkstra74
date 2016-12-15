package dijkstra.actors

import akka.actor.{Actor, ActorRef}
import dijkstra.config.RingConfig
import dijkstra.messages.{NeighbourAck, NextAnnouncement, NodeState}

abstract class DijkstraActor(id: Int, ringConfig: RingConfig) extends Actor {
  protected var state = 0
  private var next: Option[ActorRef] = None

  override def receive: Receive = {
    case NodeState(neighbourState) => receiveNeighbourState(neighbourState)
    case NextAnnouncement(neighbour) => receiveNext(neighbour)
  }

  private def receiveNeighbourState(neighbourState: Int): Unit = {
    state = recalculateState(state, neighbourState)
    print(s"$state ")
    Thread.sleep(1000)
    next.foreach(_ ! NodeState(state))
  }

  protected def recalculateState(oldState: Int, neighbourState: Int): Int

  private def receiveNext(neighbour: ActorRef): Unit = {
    next = Some(neighbour)
    sender() ! NeighbourAck
  }
}
