package dijkstra.actors

import akka.actor.{Actor, ActorRef}
import dijkstra.messages.{Privilege, StateReport}

import scala.concurrent.duration.FiniteDuration

class PrivilegeConsumer(id: Int, logger: ActorRef) extends Actor {
  override def receive: Receive = {
    case privilege: Privilege => receivePrivilege(privilege)

  }

  private def receivePrivilege(privilege: Privilege): Unit = {
    logger ! StateReport(privilege.state, id)
    val privilegeOwner = sender()
    context.system.scheduler.scheduleOnce(FiniteDuration(500, "milliseconds"), privilegeOwner, privilege)
  }
}
