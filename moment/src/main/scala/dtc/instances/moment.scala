package dtc.instances

import java.time.{DayOfWeek, Duration, LocalDate, LocalTime}

import dtc.js._
import dtc.{Local, Offset, TimeZoneId, Zoned}

object moment {
  implicit val momentZonedDTC: Zoned[MomentZonedDateTime] =
    new Zoned[MomentZonedDateTime] {
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
      def plusMonths(x: MomentZonedDateTime, months: Int): MomentZonedDateTime = x.plusMonths(months)
      def plusYears(x: MomentZonedDateTime, years: Int): MomentZonedDateTime = x.plusYears(years)

      def now(zone: TimeZoneId): MomentZonedDateTime = MomentZonedDateTime.now(zone)

      def offset(x: MomentZonedDateTime): Offset = x.offset

      def withYear(x: MomentZonedDateTime, year: Int): MomentZonedDateTime = x.withYear(year)
      def withMonth(x: MomentZonedDateTime, month: Int): MomentZonedDateTime = x.withMonth(month)
      def withDayOfMonth(x: MomentZonedDateTime, dayOfMonth: Int): MomentZonedDateTime = x.withDayOfMonth(dayOfMonth)
      def withHour(x: MomentZonedDateTime, hour: Int): MomentZonedDateTime = x.withHour(hour)
      def withMinute(x: MomentZonedDateTime, minute: Int): MomentZonedDateTime = x.withMinute(minute)
      def withSecond(x: MomentZonedDateTime, second: Int): MomentZonedDateTime = x.withSecond(second)
      def withMillisecond(x: MomentZonedDateTime, millisecond: Int): MomentZonedDateTime =
        x.withMillisecond(millisecond)

      def dayOfWeek(x: MomentZonedDateTime): DayOfWeek = x.dayOfWeek
      def dayOfMonth(x: MomentZonedDateTime): Int = x.dayOfMonth
      def month(x: MomentZonedDateTime): Int = x.month
      def year(x: MomentZonedDateTime): Int = x.year
      def millisecond(x: MomentZonedDateTime): Int = x.millisecond
      def second(x: MomentZonedDateTime): Int = x.second
      def minute(x: MomentZonedDateTime): Int = x.minute
      def hour(x: MomentZonedDateTime): Int = x.hour

      def yearsUntil(x: MomentZonedDateTime, until: MomentZonedDateTime): Long = x.yearsUntil(until)
      def monthsUntil(x: MomentZonedDateTime, until: MomentZonedDateTime): Long = x.monthsUntil(until)
      def daysUntil(x: MomentZonedDateTime, until: MomentZonedDateTime): Long = x.daysUntil(until)
      def hoursUntil(x: MomentZonedDateTime, until: MomentZonedDateTime): Long = x.hoursUntil(until)
      def minutesUntil(x: MomentZonedDateTime, until: MomentZonedDateTime): Long = x.minutesUntil(until)
      def secondsUntil(x: MomentZonedDateTime, until: MomentZonedDateTime): Long = x.secondsUntil(until)
      def millisecondsUntil(x: MomentZonedDateTime, until: MomentZonedDateTime): Long = x.millisecondsUntil(until)
    }

  implicit val momentLocalDTC: Local[MomentLocalDateTime] =
    new Local[MomentLocalDateTime] {
      def date(x: MomentLocalDateTime): LocalDate = x.toLocalDate
      def time(x: MomentLocalDateTime): LocalTime = x.toLocalTime

      def plus(x: MomentLocalDateTime, d: Duration): MomentLocalDateTime = x.plus(d)
      def plusMonths(x: MomentLocalDateTime, months: Int): MomentLocalDateTime = x.plusMonths(months)
      def plusYears(x: MomentLocalDateTime, years: Int): MomentLocalDateTime = x.plusYears(years)

      def compare(x: MomentLocalDateTime, y: MomentLocalDateTime): Int = MomentDateTime.compare(x, y)

      def of(date: LocalDate, time: LocalTime): MomentLocalDateTime = MomentLocalDateTime.of(date, time)
      def of(
        year: Int, month: Int, day: Int,
        hour: Int, minute: Int, second: Int, millisecond: Int): MomentLocalDateTime =
        MomentLocalDateTime.of(year, month, day, hour, minute, second, millisecond)

      def now: MomentLocalDateTime = MomentLocalDateTime.now

      def withYear(x: MomentLocalDateTime, year: Int): MomentLocalDateTime = x.withYear(year)
      def withMonth(x: MomentLocalDateTime, month: Int): MomentLocalDateTime = x.withMonth(month)
      def withDayOfMonth(x: MomentLocalDateTime, dayOfMonth: Int): MomentLocalDateTime = x.withDayOfMonth(dayOfMonth)
      def withHour(x: MomentLocalDateTime, hour: Int): MomentLocalDateTime = x.withHour(hour)
      def withMinute(x: MomentLocalDateTime, minute: Int): MomentLocalDateTime = x.withMinute(minute)
      def withSecond(x: MomentLocalDateTime, second: Int): MomentLocalDateTime = x.withSecond(second)
      def withMillisecond(x: MomentLocalDateTime, millisecond: Int): MomentLocalDateTime =
        x.withMillisecond(millisecond)

      def dayOfWeek(x: MomentLocalDateTime): DayOfWeek = x.dayOfWeek
      def dayOfMonth(x: MomentLocalDateTime): Int = x.dayOfMonth
      def month(x: MomentLocalDateTime): Int = x.month
      def year(x: MomentLocalDateTime): Int = x.year
      def millisecond(x: MomentLocalDateTime): Int = x.millisecond
      def second(x: MomentLocalDateTime): Int = x.second
      def minute(x: MomentLocalDateTime): Int = x.minute
      def hour(x: MomentLocalDateTime): Int = x.hour

      def yearsUntil(x: MomentLocalDateTime, until: MomentLocalDateTime): Long = x.yearsUntil(until)
      def monthsUntil(x: MomentLocalDateTime, until: MomentLocalDateTime): Long = x.monthsUntil(until)
      def daysUntil(x: MomentLocalDateTime, until: MomentLocalDateTime): Long = x.daysUntil(until)
      def hoursUntil(x: MomentLocalDateTime, until: MomentLocalDateTime): Long = x.hoursUntil(until)
      def minutesUntil(x: MomentLocalDateTime, until: MomentLocalDateTime): Long = x.minutesUntil(until)
      def secondsUntil(x: MomentLocalDateTime, until: MomentLocalDateTime): Long = x.secondsUntil(until)
      def millisecondsUntil(x: MomentLocalDateTime, until: MomentLocalDateTime): Long = x.millisecondsUntil(until)
    }
}
