package dtc.instances

import java.time._
import java.time.temporal.{ChronoField, ChronoUnit}

import dtc._

object localDateTimeAsZoned {

  /**
    * This is a special instance, that emulates Zoned behaviour for LocalDateTime.
    * Basically, this is a Zoned with zone always equal to UTC. All zone parameters are ignored.
    *
    * NOTE: This instant doesn't really hold all Zoned laws.
    * For example, whatever way you interact with it, zone will always equal UTC.
    */
  implicit val localDateTimeIsZoned: Zoned[LocalDateTime] =
    new Zoned[LocalDateTime] {
      def compare(x: LocalDateTime, y: LocalDateTime): Int = x.compareTo(y)

      def date(x: LocalDateTime): LocalDate = x.toLocalDate
      def time(x: LocalDateTime): LocalTime = x.toLocalTime.truncatedTo(ChronoUnit.MILLIS)

      def plus(x: LocalDateTime, d: Duration): LocalDateTime = x.plus(truncateToMillis(d))
      def minus(x: LocalDateTime, d: Duration): LocalDateTime = x.minus(truncateToMillis(d))
      def plusDays(x: LocalDateTime, days: Int): LocalDateTime = x.plusDays(days.toLong)
      def plusMonths(x: LocalDateTime, months: Int): LocalDateTime = x.plusMonths(months.toLong)
      def plusYears(x: LocalDateTime, years: Int): LocalDateTime = x.plusYears(years.toLong)

      def withYear(x: LocalDateTime, year: Int): LocalDateTime = x.withYear(year)
      def withMonth(x: LocalDateTime, month: Int): LocalDateTime = x.withMonth(month)
      def withDayOfMonth(x: LocalDateTime, dayOfMonth: Int): LocalDateTime = x.withDayOfMonth(dayOfMonth)
      def withHour(x: LocalDateTime, hour: Int): LocalDateTime = x.withHour(hour)
      def withMinute(x: LocalDateTime, minute: Int): LocalDateTime = x.withMinute(minute)
      def withSecond(x: LocalDateTime, second: Int): LocalDateTime = x.withSecond(second)
      def withMillisecond(x: LocalDateTime, millisecond: Int): LocalDateTime = x.withNano(millisToNanos(millisecond))
      def withTime(x: LocalDateTime, time: LocalTime): LocalDateTime = LocalDateTime.of(date(x), time)
      def withDate(x: LocalDateTime, date: LocalDate): LocalDateTime = LocalDateTime.of(date, time(x))

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

      def of(date: LocalDate, time: LocalTime, zone: TimeZoneId): LocalDateTime = LocalDateTime.of(date, time)

      def withZoneSameInstant(x: LocalDateTime, zone: TimeZoneId): LocalDateTime = x

      def withZoneSameLocal(x: LocalDateTime, zone: TimeZoneId): LocalDateTime = x

      def zone(x: LocalDateTime): TimeZoneId = TimeZoneId.UTC

      def offset(x: LocalDateTime): Offset = Offset(0)
    }

}
