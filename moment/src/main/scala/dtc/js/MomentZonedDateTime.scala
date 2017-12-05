package dtc.js

import java.time.{LocalDate, LocalTime}

import dtc._
import moment.{Date, Moment, Units}

import scala.scalajs.js.Math
import scala.util.Try

/**
  * Zone aware mutability safe wrapper around momentjs Date.
  *
  * Works in tz mode, effectively providing a zoned datetime value with DST support.
  *
  * Wrapper also overrides semantics to JVM-identical.
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

  def offset: Offset = Offset(Math.round(underlying.utcOffset() * SecondsInMinute).toInt)

  def toLocal: MomentLocalDateTime = MomentLocalDateTime.of(toLocalDate, toLocalTime)

  def updated(modifier: Date => Date): MomentZonedDateTime =
    new MomentZonedDateTime(modifier(copy), zone)

  override def equals(obj: scala.Any): Boolean = obj match {
    case m: MomentZonedDateTime => MomentDateTime.compare(this, m) == 0
    case _ => false
  }

  override def hashCode() = (underlying.value() % Int.MaxValue).toInt
}

object MomentZonedDateTime {

  def now(zone: TimeZoneId): MomentZonedDateTime = new MomentZonedDateTime(Moment().tz(zone.id), zone)

  def of(
    year: Int, month: Int, day: Int,
    hour: Int, minute: Int, second: Int, millisecond: Int,
    zone: TimeZoneId): MomentZonedDateTime = {
    val date = Try(LocalDate.of(year, month, day))
    require(date.isSuccess, s"Invalid date: ${date.failed.get.getMessage}")

    val time = Try(LocalTime.of(hour, minute, second, millisToNanos(millisecond)))
    require(time.isSuccess, s"Invalid time: ${time.failed.get.getMessage}")

    of(date.get, time.get, zone)
  }

  def of(date: LocalDate, time: LocalTime, zone: TimeZoneId): MomentZonedDateTime =
    new MomentZonedDateTime(Moment.tz(MomentDateTime.constructorArray(date, time), zone.id), zone)

  def of(raw: Date, zone: TimeZoneId): MomentZonedDateTime = {
    require(raw.isValid() && raw.tz() == zone.id)
    new MomentZonedDateTime(raw, zone)
  }
}
