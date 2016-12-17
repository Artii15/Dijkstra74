package dijkstra

import akka.actor.{ActorRef, ActorSystem, Props}
import dijkstra.actors.RingSupervisor
import dijkstra.config.RingConfig
import dijkstra.messages.{NodeDisturbance, Start}

import scala.annotation.tailrec
import scala.io.StdIn

object Main {
  def main(args: Array[String]): Unit = new Main().run()
}

class Main {
  private val actorSystem = ActorSystem()
  private val config = new RingConfig(100, 10)
  private val supervisor = actorSystem.actorOf(Props(new RingSupervisor(config)))

  def run(): Unit = {
    supervisor ! Start
    interact()
  }

  @tailrec
  private def interact(): Unit = {
    StdIn.readLine() match {
      case "1" => supervisor ! NodeDisturbance; interact()
      case "2" => actorSystem.terminate()
      case _ => interact()
    }
  }
}
