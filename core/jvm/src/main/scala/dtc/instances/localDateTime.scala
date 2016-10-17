package dtc.instances

import java.time.temporal.ChronoUnit
import java.time._

import dtc._

object localDateTime {
  implicit val localDateTimeDTC: LocalDateTimeTC[LocalDateTime] =
    new LocalDateTimeTC[LocalDateTime] {
      def compare(x: LocalDateTime, y: LocalDateTime): Int = x.compareTo(y)
      def date(x: LocalDateTime): LocalDate = x.toLocalDate
      def time(x: LocalDateTime): LocalTime = x.toLocalTime
      def of(date: LocalDate, time: LocalTime): LocalDateTime =
        LocalDateTime.of(date, time.truncatedTo(ChronoUnit.MILLIS))
      def plus(x: LocalDateTime, d: Duration): LocalDateTime = x.plus(d)
      def withYear(x: LocalDateTime, year: Int): LocalDateTime = x.withYear(year)
      def withMonth(x: LocalDateTime, month: Int): LocalDateTime = x.withMonth(month)
      def withDayOfMonth(x: LocalDateTime, dayOfMonth: Int): LocalDateTime = x.withDayOfMonth(dayOfMonth)
      def withHour(x: LocalDateTime, hour: Int): LocalDateTime = x.withHour(hour)
      def withMinute(x: LocalDateTime, minute: Int): LocalDateTime = x.withMinute(minute)
      def withSecond(x: LocalDateTime, second: Int): LocalDateTime = x.withSecond(second)
      def withMillisecond(x: LocalDateTime, millisecond: Int): LocalDateTime = x.withNano(millisToNanos(millisecond))
      def now: LocalDateTime = LocalDateTime.now
      def hoursUntil(x: LocalDateTime, until: LocalDateTime): Long = x.until(until, ChronoUnit.HOURS)
      def minutesUntil(x: LocalDateTime, until: LocalDateTime): Long = x.until(until, ChronoUnit.MINUTES)
      def secondsUntil(x: LocalDateTime, until: LocalDateTime): Long = x.until(until, ChronoUnit.SECONDS)
      def millisecondsUntil(x: LocalDateTime, until: LocalDateTime): Long = x.until(until, ChronoUnit.MILLIS)
    }
}
