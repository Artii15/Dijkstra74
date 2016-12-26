package dijkstra.actors

import akka.actor.ActorRef

class OrdinaryActor(id: Int, consumer: ActorRef) extends DijkstraActor(id, consumer) {

  override protected def hasPermission(neighbourState: Int): Boolean = state != neighbourState

  override protected def calculateNewState(neighbourState: Int): Int = neighbourState
}
