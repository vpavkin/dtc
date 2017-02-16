package dtc.examples

import dtc.Local
import dtc.syntax.local._
import cats.syntax.order._

case class CalendarEvent[T: Local](startsAt: T, endsAt: T, notes: String)

case class Calendar[T: Local](events: List[CalendarEvent[T]]) {
  def eventsAfter(t: T): List[CalendarEvent[T]] = events.filter(_.startsAt > t)
  def onlyWorkDays: List[CalendarEvent[T]] = events.filter(_.startsAt.dayOfWeek.getValue > 5)
}
