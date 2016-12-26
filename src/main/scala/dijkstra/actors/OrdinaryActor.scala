package dijkstra.actors

import akka.actor.ActorRef

class OrdinaryActor(consumer: ActorRef) extends DijkstraActor(consumer) {

  override protected def hasPermission(neighbourState: Int): Boolean = state != neighbourState

  override protected def calculateNewState(neighbourState: Int): Int = neighbourState
}
