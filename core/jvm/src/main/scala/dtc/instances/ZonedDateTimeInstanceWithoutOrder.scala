package dtc.instances

import java.time._
import java.time.temporal.{ChronoField, ChronoUnit}

import dtc.{Offset, TimeZoneId, Zoned, millisToNanos, truncateToMillis}
import dtc.syntax.timeZone._

trait ZonedDateTimeInstanceWithoutOrder extends Zoned[ZonedDateTime] { self =>
  def capture(date: LocalDate, time: LocalTime, zone: TimeZoneId): ZonedDateTime =
    ZonedDateTime.of(date, time.truncatedTo(ChronoUnit.MILLIS), zone.zoneId)

  def withZoneSameInstant(x: ZonedDateTime, zone: TimeZoneId): ZonedDateTime = x.withZoneSameInstant(zone.zoneId)
  def withZoneSameLocal(x: ZonedDateTime, zone: TimeZoneId): ZonedDateTime = x.withZoneSameLocal(zone.zoneId)
  def zone(x: ZonedDateTime): TimeZoneId = TimeZoneId(x.getZone.getId)

  def date(x: ZonedDateTime): LocalDate = x.toLocalDate
  def time(x: ZonedDateTime): LocalTime = x.toLocalTime.truncatedTo(ChronoUnit.MILLIS)

  def plus(x: ZonedDateTime, d: Duration): ZonedDateTime = x.plus(truncateToMillis(d))
  def minus(x: ZonedDateTime, d: Duration): ZonedDateTime = x.minus(truncateToMillis(d))
  def plusDays(x: ZonedDateTime, days: Int): ZonedDateTime = x.plusDays(days.toLong)
  def plusMonths(x: ZonedDateTime, months: Int): ZonedDateTime = x.plusMonths(months.toLong)
  def plusYears(x: ZonedDateTime, years: Int): ZonedDateTime = x.plusYears(years.toLong)

  def withYear(x: ZonedDateTime, year: Int): ZonedDateTime = x.withYear(year)
  def withMonth(x: ZonedDateTime, month: Int): ZonedDateTime = x.withMonth(month)
  def withDayOfMonth(x: ZonedDateTime, dayOfMonth: Int): ZonedDateTime = x.withDayOfMonth(dayOfMonth)
  def withHour(x: ZonedDateTime, hour: Int): ZonedDateTime = x.withHour(hour)
  def withMinute(x: ZonedDateTime, minute: Int): ZonedDateTime = x.withMinute(minute)
  def withSecond(x: ZonedDateTime, second: Int): ZonedDateTime = x.withSecond(second)
  def withMillisecond(x: ZonedDateTime, millisecond: Int): ZonedDateTime = x.withNano(millisToNanos(millisecond))
  def withTime(x: ZonedDateTime, time: LocalTime): ZonedDateTime = ZonedDateTime.of(date(x), time, zone(x).zoneId)
  def withDate(x: ZonedDateTime, date: LocalDate): ZonedDateTime = ZonedDateTime.of(date, time(x), zone(x).zoneId)

  def offset(x: ZonedDateTime): Offset = Offset(x.getOffset.getTotalSeconds)

  def dayOfWeek(x: ZonedDateTime): DayOfWeek = x.getDayOfWeek
  def dayOfMonth(x: ZonedDateTime): Int = x.getDayOfMonth
  def month(x: ZonedDateTime): Int = x.getMonthValue
  def year(x: ZonedDateTime): Int = x.getYear
  def millisecond(x: ZonedDateTime): Int = x.get(ChronoField.MILLI_OF_SECOND)
  def second(x: ZonedDateTime): Int = x.getSecond
  def minute(x: ZonedDateTime): Int = x.getMinute
  def hour(x: ZonedDateTime): Int = x.getHour

  def yearsUntil(x: ZonedDateTime, until: ZonedDateTime): Long = x.until(until, ChronoUnit.YEARS)
  def monthsUntil(x: ZonedDateTime, until: ZonedDateTime): Long = x.until(until, ChronoUnit.MONTHS)
  def daysUntil(x: ZonedDateTime, until: ZonedDateTime): Long = x.until(until, ChronoUnit.DAYS)
  def hoursUntil(x: ZonedDateTime, until: ZonedDateTime): Long = x.until(until, ChronoUnit.HOURS)
  def minutesUntil(x: ZonedDateTime, until: ZonedDateTime): Long = x.until(until, ChronoUnit.MINUTES)
  def secondsUntil(x: ZonedDateTime, until: ZonedDateTime): Long = x.until(until, ChronoUnit.SECONDS)
  def millisecondsUntil(x: ZonedDateTime, until: ZonedDateTime): Long = x.until(until, ChronoUnit.MILLIS)

  def utc(x: ZonedDateTime): (LocalDate, LocalTime) = {
    val utcTime = x.withZoneSameInstant(ZoneOffset.UTC)
    utcTime.toLocalDate -> utcTime.toLocalTime
  }
}
