package dtc.js

import java.time.{LocalDate, LocalTime}

import dtc._
import org.widok.moment.{Date, Moment, Units}

import scala.util.Try

/**
  * Mutability safe wrapper around moment Date.
  */
class MomentZonedDateTime private(protected override val underlying: Date, val zone: TimeZoneId)
  extends MomentDateTime[MomentZonedDateTime] {

  require(underlying.isValid())

  def withZoneSameInstant(newZone: TimeZoneId): MomentZonedDateTime =
    new MomentZonedDateTime(copy.tz(newZone.id), newZone)

  def withZoneSameLocal(newZone: TimeZoneId): MomentZonedDateTime = {
    val another = copy.tz(newZone.id)

    another.add(underlying.utcOffset() - another.utcOffset(), Units.Minute)
    new MomentZonedDateTime(another, newZone)
  }

  // todo: move up to base trait after https://github.com/widok/scala-js-momentjs/pull/20 is released
  def withYear(year: Int): MomentZonedDateTime = MomentZonedDateTime.of(toLocalDate.withYear(year), toLocalTime, zone)
  def withMonth(month: Int): MomentZonedDateTime =
    MomentZonedDateTime.of(toLocalDate.withMonth(month), toLocalTime, zone)
  def withDayOfMonth(dayOfMonth: Int): MomentZonedDateTime =
    MomentZonedDateTime.of(toLocalDate.withDayOfMonth(dayOfMonth), toLocalTime, zone)
  def withHour(hour: Int): MomentZonedDateTime =
    MomentZonedDateTime.of(toLocalDate, toLocalTime.withHour(hour), zone)
  def withMinute(minute: Int): MomentZonedDateTime =
    MomentZonedDateTime.of(toLocalDate, toLocalTime.withMinute(minute), zone)
  def withSecond(second: Int): MomentZonedDateTime =
    MomentZonedDateTime.of(toLocalDate, toLocalTime.withSecond(second), zone)
  def withMillisecond(millisecond: Int): MomentZonedDateTime =
    MomentZonedDateTime.of(toLocalDate, toLocalTime.withNano(millisToNanos(millisecond)), zone)

  def plusMillis(n: Long): MomentZonedDateTime = updated(_.add(n.toDouble, Units.Millisecond))

  def updated(modifier: Date => Date): MomentZonedDateTime =
    new MomentZonedDateTime(modifier(copy), zone)
}

object MomentZonedDateTime {

  def now(zone: TimeZoneId): MomentZonedDateTime = new MomentZonedDateTime(Moment().tz(zone.id), zone)

  def of(
    year: Int, month: Int, day: Int,
    hour: Int, minute: Int = 0, second: Int = 0, millisecond: Int = 0,
    zone: TimeZoneId): MomentZonedDateTime = {
    val date = Try(LocalDate.of(year, month, day))
    require(date.isSuccess, s"Invalid date: ${date.failed.get.getMessage}")

    val time = Try(LocalTime.of(hour, minute, second, millisToNanos(millisecond)))
    require(time.isSuccess, s"Invalid time: ${time.failed.get.getMessage}")

    of(date.get, time.get, zone)
  }

  def of(date: LocalDate, time: LocalTime, zone: TimeZoneId): MomentZonedDateTime =
    new MomentZonedDateTime(utcMoment(date, time).tz(zone.id), zone)
}
