package dijkstra.actors

import akka.actor.{Actor, ActorRef}
import dijkstra.messages.{NeighbourAck, NextAnnouncement, NodeState}

abstract class DijkstraActor(id: Int, permissionConsumer: ActorRef) extends Actor {
  protected var state = 0
  private var next: Option[ActorRef] = None

  override def receive: Receive = {
    case NodeState(neighbourState) => receiveNeighbourState(neighbourState)
    case NextAnnouncement(neighbour) => receiveNext(neighbour)
  }

  private def receiveNeighbourState(neighbourState: Int): Unit = {
    if(hasPermission(neighbourState)) changeState(calculateNewState(neighbourState))
  }

  protected def hasPermission(neighbourState: Int): Boolean

  protected def calculateNewState(neighbourState: Int): Int

  private def changeState(newState: Int): Unit = {
    state = newState
    next.foreach(_ ! NodeState(newState))
  }

  private def receiveNext(neighbour: ActorRef): Unit = {
    next = Some(neighbour)
    sender() ! NeighbourAck
  }
}
