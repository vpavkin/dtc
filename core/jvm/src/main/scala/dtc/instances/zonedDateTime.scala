package dtc.instances

import java.time.temporal.ChronoUnit
import java.time._

import dtc._
import dtc.syntax.timeZone._

object zonedDateTime {
  implicit val zonedDateTimeDTC: ZonedDateTimeTC[ZonedDateTime] =
    new ZonedDateTimeTC[ZonedDateTime] {
      def of(date: LocalDate, time: LocalTime, zone: TimeZoneId): ZonedDateTime =
        ZonedDateTime.of(date, time, zone.zoneId)
      def withZoneSameInstant(x: ZonedDateTime, zone: TimeZoneId): ZonedDateTime = x.withZoneSameInstant(zone.zoneId)
      def withZoneSameLocal(x: ZonedDateTime, zone: TimeZoneId): ZonedDateTime = x.withZoneSameLocal(zone.zoneId)
      def zone(x: ZonedDateTime): TimeZoneId = TimeZoneId(x.getZone.getId)
      def date(x: ZonedDateTime): LocalDate = x.toLocalDate
      def time(x: ZonedDateTime): LocalTime = x.toLocalTime
      def compare(x: ZonedDateTime, y: ZonedDateTime): Int = x.compareTo(y)
      def plus(x: ZonedDateTime, d: Duration): ZonedDateTime = x.plus(d)
      def withYear(x: ZonedDateTime, year: Int): ZonedDateTime = x.withYear(year)
      def withMonth(x: ZonedDateTime, month: Int): ZonedDateTime = x.withMonth(month)
      def withDayOfMonth(x: ZonedDateTime, dayOfMonth: Int): ZonedDateTime = x.withDayOfMonth(dayOfMonth)
      def withHour(x: ZonedDateTime, hour: Int): ZonedDateTime = x.withHour(hour)
      def withMinute(x: ZonedDateTime, minute: Int): ZonedDateTime = x.withMinute(minute)
      def withSecond(x: ZonedDateTime, second: Int): ZonedDateTime = x.withSecond(second)
      def withMillisecond(x: ZonedDateTime, millisecond: Int): ZonedDateTime = x.withNano(millisToNanos(millisecond))
      def now(zone: TimeZoneId): ZonedDateTime = ZonedDateTime.now(zone.zoneId)
      def hoursUntil(x: ZonedDateTime, until: ZonedDateTime): Long = x.until(until, ChronoUnit.HOURS)
      def minutesUntil(x: ZonedDateTime, until: ZonedDateTime): Long = x.until(until, ChronoUnit.MINUTES)
      def secondsUntil(x: ZonedDateTime, until: ZonedDateTime): Long = x.until(until, ChronoUnit.SECONDS)
      def millisecondsUntil(x: ZonedDateTime, until: ZonedDateTime): Long = x.until(until, ChronoUnit.MILLIS)
    }
}
