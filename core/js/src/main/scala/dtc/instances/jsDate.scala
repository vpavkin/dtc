package dtc.instances

import java.time.{Duration, LocalDate, LocalTime}

import dtc.LocalDateTimeTC
import dtc.js.JSDate

object jsDate {
  implicit val jsDateLocalDTC: LocalDateTimeTC[JSDate] =
    new LocalDateTimeTC[JSDate] {
      def compare(x: JSDate, y: JSDate): Int = JSDate.compare(x, y)
      def of(date: LocalDate, time: LocalTime): JSDate = JSDate.of(date, time)
      def date(x: JSDate): LocalDate = x.toLocalDate
      def time(x: JSDate): LocalTime = x.toLocalTime
      def plus(x: JSDate, d: Duration): JSDate = x.plus(d)
    }
}
