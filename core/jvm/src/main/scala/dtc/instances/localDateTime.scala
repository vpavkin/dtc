package dtc.instances

import java.time.temporal.{ChronoField, ChronoUnit}
import java.time._

import dtc._

object localDateTime {
  implicit val localDateTimeDTC: Local[LocalDateTime] =
    new Local[LocalDateTime] {
      def compare(x: LocalDateTime, y: LocalDateTime): Int = x.compareTo(y)

      def date(x: LocalDateTime): LocalDate = x.toLocalDate
      def time(x: LocalDateTime): LocalTime = x.toLocalTime.truncatedTo(ChronoUnit.MILLIS)

      def of(date: LocalDate, time: LocalTime): LocalDateTime =
        LocalDateTime.of(date, time.truncatedTo(ChronoUnit.MILLIS))
      def of(
        year: Int, month: Int, day: Int,
        hour: Int, minute: Int, second: Int, millisecond: Int): LocalDateTime =
        LocalDateTime.of(year, month, day, hour, minute, second, millisToNanos(millisecond))

      def plus(x: LocalDateTime, d: Duration): LocalDateTime = x.plus(truncateToMillis(d))
      def minus(x: LocalDateTime, d: Duration): LocalDateTime = x.minus(truncateToMillis(d))
      def plusMonths(x: LocalDateTime, months: Int): LocalDateTime = x.plusMonths(months.toLong)
      def plusYears(x: LocalDateTime, years: Int): LocalDateTime = x.plusYears(years.toLong)

      def withYear(x: LocalDateTime, year: Int): LocalDateTime = x.withYear(year)
      def withMonth(x: LocalDateTime, month: Int): LocalDateTime = x.withMonth(month)
      def withDayOfMonth(x: LocalDateTime, dayOfMonth: Int): LocalDateTime = x.withDayOfMonth(dayOfMonth)
      def withHour(x: LocalDateTime, hour: Int): LocalDateTime = x.withHour(hour)
      def withMinute(x: LocalDateTime, minute: Int): LocalDateTime = x.withMinute(minute)
      def withSecond(x: LocalDateTime, second: Int): LocalDateTime = x.withSecond(second)
      def withMillisecond(x: LocalDateTime, millisecond: Int): LocalDateTime = x.withNano(millisToNanos(millisecond))

      def dayOfWeek(x: LocalDateTime): DayOfWeek = x.getDayOfWeek
      def dayOfMonth(x: LocalDateTime): Int = x.getDayOfMonth
      def month(x: LocalDateTime): Int = x.getMonthValue
      def year(x: LocalDateTime): Int = x.getYear
      def millisecond(x: LocalDateTime): Int = x.get(ChronoField.MILLI_OF_SECOND)
      def second(x: LocalDateTime): Int = x.getSecond
      def minute(x: LocalDateTime): Int = x.getMinute
      def hour(x: LocalDateTime): Int = x.getHour

      def yearsUntil(x: LocalDateTime, until: LocalDateTime): Long = x.until(until, ChronoUnit.YEARS)
      def monthsUntil(x: LocalDateTime, until: LocalDateTime): Long = x.until(until, ChronoUnit.MONTHS)
      def daysUntil(x: LocalDateTime, until: LocalDateTime): Long = x.until(until, ChronoUnit.DAYS)
      def hoursUntil(x: LocalDateTime, until: LocalDateTime): Long = x.until(until, ChronoUnit.HOURS)
      def minutesUntil(x: LocalDateTime, until: LocalDateTime): Long = x.until(until, ChronoUnit.MINUTES)
      def secondsUntil(x: LocalDateTime, until: LocalDateTime): Long = x.until(until, ChronoUnit.SECONDS)
      def millisecondsUntil(x: LocalDateTime, until: LocalDateTime): Long = x.until(until, ChronoUnit.MILLIS)
    }
}
