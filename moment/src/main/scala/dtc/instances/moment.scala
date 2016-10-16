package dtc.instances

import java.time.{Duration, LocalDate, LocalTime}

import dtc.js._
import dtc.{LocalDateTimeTC, TimeZoneId, ZonedDateTimeTC}

object moment {
  implicit val momentZonedDTC: ZonedDateTimeTC[MomentZonedDateTime] =
    new ZonedDateTimeTC[MomentZonedDateTime] {
      def of(date: LocalDate, time: LocalTime, zone: TimeZoneId): MomentZonedDateTime =
        MomentZonedDateTime.of(date, time, zone)
      def withZoneSameInstant(x: MomentZonedDateTime, zone: TimeZoneId): MomentZonedDateTime =
        x.withZoneSameInstant(zone)
      def withZoneSameLocal(x: MomentZonedDateTime, zone: TimeZoneId): MomentZonedDateTime = x.withZoneSameLocal(zone)
      def zone(x: MomentZonedDateTime): TimeZoneId = x.zone
      def date(x: MomentZonedDateTime): LocalDate = x.toLocalDate
      def time(x: MomentZonedDateTime): LocalTime = x.toLocalTime
      def compare(x: MomentZonedDateTime, y: MomentZonedDateTime): Int = MomentDateTime.compare(x, y)
      def plus(x: MomentZonedDateTime, d: Duration): MomentZonedDateTime = x.plus(d)
      def now(zone: TimeZoneId): MomentZonedDateTime = MomentZonedDateTime.now(zone)
      def withYear(x: MomentZonedDateTime, year: Int): MomentZonedDateTime = x.withYear(year)
      def withMonth(x: MomentZonedDateTime, month: Int): MomentZonedDateTime = x.withMonth(month)
      def withDayOfMonth(x: MomentZonedDateTime, dayOfMonth: Int): MomentZonedDateTime = x.withDayOfMonth(dayOfMonth)
      def withHour(x: MomentZonedDateTime, hour: Int): MomentZonedDateTime = x.withHour(hour)
      def withMinute(x: MomentZonedDateTime, minute: Int): MomentZonedDateTime = x.withMinute(minute)
      def withSecond(x: MomentZonedDateTime, second: Int): MomentZonedDateTime = x.withSecond(second)
      def withMillisecond(x: MomentZonedDateTime, millisecond: Int): MomentZonedDateTime =
        x.withMillisecond(millisecond)
      def hoursUntil(x: MomentZonedDateTime, until: MomentZonedDateTime): Long = x.hoursUntil(until)
      def minutesUntil(x: MomentZonedDateTime, until: MomentZonedDateTime): Long = x.minutesUntil(until)
      def secondsUntil(x: MomentZonedDateTime, until: MomentZonedDateTime): Long = x.secondsUntil(until)
      def millisecondsUntil(x: MomentZonedDateTime, until: MomentZonedDateTime): Long = x.millisecondsUntil(until)
    }

  implicit val momentLocalDTC: LocalDateTimeTC[MomentLocalDateTime] =
    new LocalDateTimeTC[MomentLocalDateTime] {
      def date(x: MomentLocalDateTime): LocalDate = x.toLocalDate
      def time(x: MomentLocalDateTime): LocalTime = x.toLocalTime
      def plus(x: MomentLocalDateTime, d: Duration): MomentLocalDateTime = x.plus(d)
      def compare(x: MomentLocalDateTime, y: MomentLocalDateTime): Int = MomentDateTime.compare(x, y)
      def of(date: LocalDate, time: LocalTime): MomentLocalDateTime = MomentLocalDateTime.of(date, time)
      def now: MomentLocalDateTime = MomentLocalDateTime.now
      def withYear(x: MomentLocalDateTime, year: Int): MomentLocalDateTime = x.withYear(year)
      def withMonth(x: MomentLocalDateTime, month: Int): MomentLocalDateTime = x.withMonth(month)
      def withDayOfMonth(x: MomentLocalDateTime, dayOfMonth: Int): MomentLocalDateTime = x.withDayOfMonth(dayOfMonth)
      def withHour(x: MomentLocalDateTime, hour: Int): MomentLocalDateTime = x.withHour(hour)
      def withMinute(x: MomentLocalDateTime, minute: Int): MomentLocalDateTime = x.withMinute(minute)
      def withSecond(x: MomentLocalDateTime, second: Int): MomentLocalDateTime = x.withSecond(second)
      def withMillisecond(x: MomentLocalDateTime, millisecond: Int): MomentLocalDateTime =
        x.withMillisecond(millisecond)
      def hoursUntil(x: MomentLocalDateTime, until: MomentLocalDateTime): Long = x.hoursUntil(until)
      def minutesUntil(x: MomentLocalDateTime, until: MomentLocalDateTime): Long = x.minutesUntil(until)
      def secondsUntil(x: MomentLocalDateTime, until: MomentLocalDateTime): Long = x.secondsUntil(until)
      def millisecondsUntil(x: MomentLocalDateTime, until: MomentLocalDateTime): Long = x.millisecondsUntil(until)
    }
}
