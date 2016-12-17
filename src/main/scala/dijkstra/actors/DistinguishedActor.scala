package dijkstra.actors

import dijkstra.config.RingConfig
import dijkstra.messages.NodeState

class DistinguishedActor(id: Int, config: RingConfig) extends DijkstraActor(id, config) {

  override protected def onStateReceive(neighbourState: Int): Unit = {
    showState(state, neighbourState)
    Thread.sleep(500)
    state = recalculateState(state, neighbourState)
    next.foreach(_ ! NodeState(state))
  }

  private def recalculateState(oldState: Int, neighbourState: Int): Int =
    if (oldState == neighbourState) (oldState + 1) % config.K else oldState

  private def showState(state: Int, neighbourState: Int): Unit = {
    println(s"${Console.RESET}")
    print(s"${if (state != neighbourState) Console.CYAN_B else Console.RED_B}$state")
    Console.flush()
  }
}
