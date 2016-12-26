package dijkstra.actors

import akka.actor.Actor
import dijkstra.messages.StateReport
import misra.demo.Writer

import scala.collection.mutable

class StateLogger extends Actor {
  private val nodesStates: mutable.SortedMap[Int, Int] = mutable.SortedMap()

  override def receive: Receive = {
    case stateReport: StateReport => reportState(stateReport)
  }

  private def reportState(stateReport: StateReport): Unit = {
    nodesStates(stateReport.reporterId) = stateReport.state
    printLine(1, nodesStates.keys)
    printLine(2, nodesStates.values)
  }

  private def printLine(row: Int, numbers: Iterable[Int]): Unit = {
    Writer.setCursorPosition(row, 1)
    Writer.clearLine()
    println(numbers.map(state => f"$state%4d").mkString(" "))
  }
}
