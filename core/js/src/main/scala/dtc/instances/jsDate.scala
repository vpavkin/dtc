package dtc.instances

import java.time._

import dtc.Local
import dtc.js.JSDate

object jsDate {
  implicit val jsDateLocalDTC: Local[JSDate] =
    new Local[JSDate] {
      def compare(x: JSDate, y: JSDate): Int = JSDate.compare(x, y)

      def of(date: LocalDate, time: LocalTime): JSDate = JSDate.of(date, time)
      def of(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int, millisecond: Int): JSDate =
        JSDate.of(year, month, day, hour, minute, second, millisecond)

      def date(x: JSDate): LocalDate = x.toLocalDate
      def time(x: JSDate): LocalTime = x.toLocalTime

      def plus(x: JSDate, d: Duration): JSDate = x.plus(d)
      def plusMonths(x: JSDate, months: Int): JSDate = x.plusMonths(months)
      def plusYears(x: JSDate, years: Int): JSDate = x.plusYears(years)

      def withYear(x: JSDate, year: Int): JSDate = x.withYear(year)
      def withMonth(x: JSDate, month: Int): JSDate = x.withMonth(month)
      def withDayOfMonth(x: JSDate, dayOfMonth: Int): JSDate = x.withDayOfMonth(dayOfMonth)
      def withHour(x: JSDate, hour: Int): JSDate = x.withHour(hour)
      def withMinute(x: JSDate, minute: Int): JSDate = x.withMinute(minute)
      def withSecond(x: JSDate, second: Int): JSDate = x.withSecond(second)
      def withMillisecond(x: JSDate, millisecond: Int): JSDate = x.withMillisecond(millisecond)

      def dayOfWeek(x: JSDate): DayOfWeek = x.dayOfWeek
      def dayOfMonth(x: JSDate): Int = x.dayOfMonth
      def month(x: JSDate): Int = x.month
      def year(x: JSDate): Int = x.year
      def millisecond(x: JSDate): Int = x.millisecond
      def second(x: JSDate): Int = x.second
      def minute(x: JSDate): Int = x.minute
      def hour(x: JSDate): Int = x.hour

      def yearsUntil(x: JSDate, until: JSDate): Long = x.yearsUntil(until)
      def monthsUntil(x: JSDate, until: JSDate): Long = x.monthsUntil(until)
      def daysUntil(x: JSDate, until: JSDate): Long = x.daysUntil(until)
      def hoursUntil(x: JSDate, until: JSDate): Long = x.hoursUntil(until)
      def minutesUntil(x: JSDate, until: JSDate): Long = x.minutesUntil(until)
      def secondsUntil(x: JSDate, until: JSDate): Long = x.secondsUntil(until)
      def millisecondsUntil(x: JSDate, until: JSDate): Long = x.millisecondsUntil(until)
    }
}
