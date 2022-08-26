package dtc.instances.moment

import java.time.{DayOfWeek, Duration, LocalDate, LocalTime, Period}
import dtc.js.MomentZonedDateTime
import dtc.{Offset, TimeZoneId, Zoned}

trait MomentZonedDateTimeInstanceWithoutOrder extends Zoned[MomentZonedDateTime] {
  def capture(date: LocalDate, time: LocalTime, zone: TimeZoneId): MomentZonedDateTime =
    MomentZonedDateTime.of(date, time, zone)

  def withZoneSameInstant(x: MomentZonedDateTime, zone: TimeZoneId): MomentZonedDateTime =
    x.withZoneSameInstant(zone)
  def withZoneSameLocal(x: MomentZonedDateTime, zone: TimeZoneId): MomentZonedDateTime = x.withZoneSameLocal(zone)
  def zone(x: MomentZonedDateTime): TimeZoneId = x.zone

  def date(x: MomentZonedDateTime): LocalDate = x.toLocalDate
  def time(x: MomentZonedDateTime): LocalTime = x.toLocalTime

  def plus(x: MomentZonedDateTime, d: Duration): MomentZonedDateTime = x.plus(d)
  def minus(x: MomentZonedDateTime, d: Duration): MomentZonedDateTime = x.minus(d)
  def plusDays(x: MomentZonedDateTime, days: Int): MomentZonedDateTime = x.plusDays(days)
  def plusMonths(x: MomentZonedDateTime, months: Int): MomentZonedDateTime = x.plusMonths(months)
  def plusYears(x: MomentZonedDateTime, years: Int): MomentZonedDateTime = x.plusYears(years)

  def offset(x: MomentZonedDateTime): Offset = x.offset

  def withYear(x: MomentZonedDateTime, year: Int): MomentZonedDateTime = x.withYear(year)
  def withMonth(x: MomentZonedDateTime, month: Int): MomentZonedDateTime = x.withMonth(month)
  def withDayOfMonth(x: MomentZonedDateTime, dayOfMonth: Int): MomentZonedDateTime = x.withDayOfMonth(dayOfMonth)
  def withHour(x: MomentZonedDateTime, hour: Int): MomentZonedDateTime = x.withHour(hour)
  def withMinute(x: MomentZonedDateTime, minute: Int): MomentZonedDateTime = x.withMinute(minute)
  def withSecond(x: MomentZonedDateTime, second: Int): MomentZonedDateTime = x.withSecond(second)
  def withMillisecond(x: MomentZonedDateTime, millisecond: Int): MomentZonedDateTime =
    x.withMillisecond(millisecond)
  def withTime(x: MomentZonedDateTime, time: LocalTime): MomentZonedDateTime = x.withTime(time)
  def withDate(x: MomentZonedDateTime, date: LocalDate): MomentZonedDateTime = x.withDate(date)

  def dayOfWeek(x: MomentZonedDateTime): DayOfWeek = x.dayOfWeek
  def dayOfMonth(x: MomentZonedDateTime): Int = x.dayOfMonth
  def month(x: MomentZonedDateTime): Int = x.month
  def year(x: MomentZonedDateTime): Int = x.year
  def millisecond(x: MomentZonedDateTime): Int = x.millisecond
  def second(x: MomentZonedDateTime): Int = x.second
  def minute(x: MomentZonedDateTime): Int = x.minute
  def hour(x: MomentZonedDateTime): Int = x.hour

  def yearsUntil(x: MomentZonedDateTime, until: MomentZonedDateTime): Long = x.yearsUntil(until)
  def monthsUntil(x: MomentZonedDateTime, until: MomentZonedDateTime): Long =
    Period.between(x.toLocalDate, until.toLocalDate).toTotalMonths
  def daysUntil(x: MomentZonedDateTime, until: MomentZonedDateTime): Long = x.daysUntil(until)
  def hoursUntil(x: MomentZonedDateTime, until: MomentZonedDateTime): Long = x.hoursUntil(until)
  def minutesUntil(x: MomentZonedDateTime, until: MomentZonedDateTime): Long = x.minutesUntil(until)
  def secondsUntil(x: MomentZonedDateTime, until: MomentZonedDateTime): Long = x.secondsUntil(until)
  def millisecondsUntil(x: MomentZonedDateTime, until: MomentZonedDateTime): Long = x.millisecondsUntil(until)

  def utc(x: MomentZonedDateTime): (LocalDate, LocalTime) = {
    val utcTime = x.withZoneSameInstant(TimeZoneId.UTC)
    utcTime.toLocalDate -> utcTime.toLocalTime
  }
}
