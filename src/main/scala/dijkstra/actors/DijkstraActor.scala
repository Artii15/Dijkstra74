package dijkstra.actors

import akka.actor.{Actor, ActorRef}
import dijkstra.messages._

abstract class DijkstraActor(permissionConsumer: ActorRef) extends Actor {
  protected var state = 0
  private var next: Option[ActorRef] = None

  override def receive: Receive = {
    case NodeState(neighbourState) => receiveNeighbourState(neighbourState)
    case NextAnnouncement(neighbour) => receiveNext(neighbour)
    case ForcedStateChange(forcedState) => beginStateChange(forcedState)
    case Privilege(stateToSet) => finishStateChange(stateToSet)
  }

  private def receiveNeighbourState(neighbourState: Int): Unit = {
    if(hasPermission(neighbourState)) beginStateChange(calculateNewState(neighbourState))
  }

  protected def hasPermission(neighbourState: Int): Boolean

  protected def calculateNewState(neighbourState: Int): Int

  private def beginStateChange(newState: Int): Unit = permissionConsumer ! Privilege(newState)

  private def finishStateChange(stateToSet: Int): Unit = {
    state = stateToSet
    next.foreach(_ ! NodeState(stateToSet))
  }

  private def receiveNext(neighbour: ActorRef): Unit = {
    next = Some(neighbour)
    sender() ! NeighbourAck
  }
}
