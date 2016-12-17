package dijkstra.actors

import dijkstra.config.RingConfig

class DistinguishedActor(id: Int, config: RingConfig) extends DijkstraActor(id, config) {
  override protected def recalculateState(oldState: Int, neighbourState: Int): Int =
    if (oldState == neighbourState) (oldState + 1) % config.K else oldState

  override protected def showState(oldState: Int): Unit = {
    print(s"\n$oldState")
    Console.flush()
  }
}
