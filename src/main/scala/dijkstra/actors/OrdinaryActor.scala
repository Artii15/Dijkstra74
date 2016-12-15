package dijkstra.actors

class OrdinaryActor(id: Int) extends DijkstraActor(id) {
  override protected def recalculateState(oldState: Int, neighbourState: Int): Int =
    if (neighbourState != oldState) neighbourState else oldState
}
