package dijkstra.messages

import akka.actor.ActorRef

case class NextAnnouncement(neighbour: ActorRef)
