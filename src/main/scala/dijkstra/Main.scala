package dijkstra

import akka.actor.{ActorSystem, Props}
import dijkstra.actors.RingSupervisor
import dijkstra.config.RingConfig
import dijkstra.messages.Start

import scala.io.StdIn

object Main {
  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem()
    val config = new RingConfig(10, 10)

    val supervisor = actorSystem.actorOf(Props(new RingSupervisor(config)))
    supervisor ! Start

    StdIn.readLine()
    actorSystem.terminate()
  }
}
