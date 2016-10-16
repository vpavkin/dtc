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
      def withYear(x: JSDate, year: Int): JSDate = x.withYear(year)
      def withMonth(x: JSDate, month: Int): JSDate = x.withMonth(month)
      def withDayOfMonth(x: JSDate, dayOfMonth: Int): JSDate = x.withDayOfMonth(dayOfMonth)
      def withHour(x: JSDate, hour: Int): JSDate = x.withHour(hour)
      def withMinute(x: JSDate, minute: Int): JSDate = x.withMinute(minute)
      def withSecond(x: JSDate, second: Int): JSDate = x.withSecond(second)
      def withMillisecond(x: JSDate, millisecond: Int): JSDate = x.withMillisecond(millisecond)
      def now: JSDate = JSDate.now
      def hoursUntil(x: JSDate, until: JSDate): Long = x.hoursUntil(until)
      def minutesUntil(x: JSDate, until: JSDate): Long = x.minutesUntil(until)
      def secondsUntil(x: JSDate, until: JSDate): Long = x.secondsUntil(until)
      def millisecondsUntil(x: JSDate, until: JSDate): Long = x.millisecondsUntil(until)
    }
}
