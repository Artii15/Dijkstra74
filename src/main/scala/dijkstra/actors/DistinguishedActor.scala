package dijkstra.actors

import dijkstra.config.RingConfig

class DistinguishedActor(id: Int, ringConfig: RingConfig) extends DijkstraActor(id) {
  override protected def recalculateState(oldState: Int, neighbourState: Int): Int =
    if (oldState == neighbourState) (oldState + 1) % ringConfig.K else oldState

}
