package dijkstra.actors

import dijkstra.config.RingConfig

class OrdinaryActor(id: Int, config: RingConfig) extends DijkstraActor(id, config) {
  override protected def recalculateState(oldState: Int, neighbourState: Int): Int =
    if (neighbourState != oldState) neighbourState else oldState

  override protected def showState(state: Int, neighbourState: Int): Unit = {
    print(s" ${if (state == neighbourState) Console.CYAN_B else Console.RED_B}$state")
    Console.flush()
  }
}
