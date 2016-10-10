package dtc.js

import java.time.temporal.ChronoField
import java.time.{LocalDate, LocalTime}

import dtc.TimeZoneId
import org.widok.moment.{Date, Moment, Units}

import scalajs.js.Array
import scala.util.Try

/**
  * Mutability safe wrapper around moment Date.
  */
class MomentDateTime private(private val underlying: Date, val zone: TimeZoneId) {

  require(underlying.isValid())

  def copy = Moment(underlying)

  def withZoneSameInstant(newZone: TimeZoneId): MomentDateTime =
    new MomentDateTime(copy.tz(newZone.id), newZone)

  def withZoneSameLocal(newZone: TimeZoneId): MomentDateTime = {
    val another = copy.tz(newZone.id)

    another.add(underlying.utcOffset() - another.utcOffset(), Units.Minute)
    new MomentDateTime(another, newZone)
  }

  def jsGetTime = underlying.value()

  def updated(modifier: Date => Date): MomentDateTime =
    new MomentDateTime(modifier(copy), zone)


  def dayOfMonth: Int = underlying.date()
  def month: Int = underlying.month() + 1
  def year: Int = underlying.year()
  def hour: Int = underlying.hour()
  def minute: Int = underlying.minute()
  def second: Int = underlying.second()

  def toLocalDate: LocalDate = LocalDate.of(year, month, dayOfMonth)
  def toLocalTime: LocalTime = LocalTime.of(hour, minute, second)

  override def toString: String = underlying.toString
}

object MomentDateTime {

  def now(zone: TimeZoneId): MomentDateTime = new MomentDateTime(Moment().tz(zone.id), zone)

  def compare(x: MomentDateTime, y: MomentDateTime) =
    Ordering.Double.compare(x.underlying.value(), y.underlying.value())

  def of(
    year: Int, month: Int, day: Int,
    hour: Int, minute: Int = 0, second: Int = 0, millisecond: Int = 0,
    zone: TimeZoneId): MomentDateTime = {
    val date = Try(LocalDate.of(year, month, day))
    require(date.isSuccess, s"Invalid date: ${date.failed.get.getMessage}")

    val time = Try(LocalTime.of(hour, minute, second, millisecond * 1000))
    require(time.isSuccess, s"Invalid time: ${time.failed.get.getMessage}")

    of(date.get, time.get, zone)
  }


  def of(date: LocalDate, time: LocalTime, zone: TimeZoneId): MomentDateTime =
    new MomentDateTime(Moment.utc(Array(
      date.getYear, date.getMonthValue - 1, date.getDayOfMonth,
      time.getHour, time.getMinute, time.getSecond, time.get(ChronoField.MILLI_OF_SECOND)
    )).tz(zone.id), zone)

}
