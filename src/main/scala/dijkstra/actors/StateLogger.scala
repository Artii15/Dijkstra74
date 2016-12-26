package dijkstra.actors

import akka.actor.Actor
import dijkstra.messages.StateReport
import misra.demo.Writer

import scala.collection.mutable

class StateLogger extends Actor {
  private val nodesStates: mutable.SortedMap[Int, Int] = mutable.SortedMap.empty

  override def receive: Receive = {
    case stateReport: StateReport => reportState(stateReport)
  }

  private def reportState(stateReport: StateReport): Unit = {
    nodesStates(stateReport.reporterId) = stateReport.state
    printLine(1, Console.RED, nodesStates.keys.toList)
    printLine(2, Console.BLUE, nodesStates.values)
  }

  private def printLine(row: Int, color: String, numbers: Iterable[Int]): Unit = {
    Writer.setCursorPosition(row, 1)
    Writer.clearLine()
    val numbersLine = numbers.map(number => f"$number%4d").mkString(" ")
    println(s"$color$numbersLine${Console.RESET}")
  }
}
