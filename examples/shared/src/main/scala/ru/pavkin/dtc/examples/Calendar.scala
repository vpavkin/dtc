package ru.pavkin.dtc.examples

import ru.pavkin.dtc.LocalDateTimeTC
import ru.pavkin.dtc.syntax.localDateTime._
import cats.syntax.order._

case class CalendarEvent[T: LocalDateTimeTC](startsAt: T, endsAt: T, notes: String)

case class Calendar[T: LocalDateTimeTC](events: List[CalendarEvent[T]]) {
  def eventsAfter(t: T): List[CalendarEvent[T]] = events.filter(_.startsAt > t)
  def onlyWorkDays: List[CalendarEvent[T]] = events.filter(_.startsAt.dayOfWeek.getValue > 5)
}
