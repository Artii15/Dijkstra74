package dijkstra.actors

import akka.actor.ActorRef
import dijkstra.config.RingConfig

class DistinguishedActor(consumer: ActorRef, config: RingConfig) extends DijkstraActor(consumer) {

  override protected def hasPermission(neighbourState: Int): Boolean = state == neighbourState

  override protected def calculateNewState(neighbourState: Int): Int = (state + 1) % config.K
}
