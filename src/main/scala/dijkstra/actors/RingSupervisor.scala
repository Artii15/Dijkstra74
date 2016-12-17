package dijkstra.actors

import akka.actor.{Actor, ActorRef, Props}
import dijkstra.config.RingConfig
import dijkstra.messages._

class RingSupervisor(config: RingConfig) extends Actor {
  private var nodes:List[ActorRef] = Nil
  private var numberOfUninitializedNodes = config.numberOfNodes

  override def receive: Receive = {
    case Start => init()
    case NeighbourAck => receiveNeighbourAck()
    case NodeDisturbance => context.children.foreach(_ ! NodeDisturbance)
  }

  private def init(): Unit = {
    val distinguishedNode = context.actorOf(Props(classOf[DistinguishedActor], 1, config))
    val ordinaryNodes = Range.inclusive(2, config.numberOfNodes)
      .map(nodeId => context.actorOf(Props(classOf[OrdinaryActor], nodeId, config)))

    val lastOrdinaryNode = ordinaryNodes.foldLeft(distinguishedNode){ (previous, current) => {
      previous ! NextAnnouncement(current); current
    }}
    lastOrdinaryNode ! NextAnnouncement(distinguishedNode)
    nodes = distinguishedNode :: ordinaryNodes.toList
  }

  private def receiveNeighbourAck(): Unit = {
    numberOfUninitializedNodes -= 1
    if(numberOfUninitializedNodes == 0) nodes.head ! NodeState(0)
  }
}
