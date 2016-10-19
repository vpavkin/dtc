package dtc.js

import java.time.{LocalDate, LocalTime}

import dtc._
import dtc.js.MomentDateTime.utcMoment
import moment.{Date, Moment, Units}

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
