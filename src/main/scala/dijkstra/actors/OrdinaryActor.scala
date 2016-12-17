package dijkstra.actors

import dijkstra.config.RingConfig
import dijkstra.messages.NodeState

class OrdinaryActor(id: Int, config: RingConfig) extends DijkstraActor(id, config) {

  override def onStateReceive(neighbourState: Int): Unit = {
    showState(state, neighbourState)
    Thread.sleep(500)
    next.foreach(_ ! NodeState(state))
    state = recalculateState(state, neighbourState)
  }

  private def recalculateState(oldState: Int, neighbourState: Int): Int =
    if (neighbourState != oldState) neighbourState else oldState

  private def showState(state: Int, neighbourState: Int): Unit = {
    print(s" ${if (state == neighbourState) Console.CYAN_B else Console.RED_B}$state")
    Console.flush()
  }
}
