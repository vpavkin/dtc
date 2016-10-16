package dtc.js

import java.time.temporal.ChronoField
import java.time.{Duration, LocalDate, LocalTime}

import dtc._

import scala.scalajs.js.Date
import scala.util.Try

/**
  * Mutability safe wrapper around plain JS Date.
  *
  * Shouldn't be used as a standalone thing as API is just enough
  * to fit [[dtc.LocalDateTimeTC]] typeclass requirements.
  *
  * Supports only [[dtc.LocalDateTimeTC]] typeclass due to weak time-zone capabilities.
  */
class JSDate private(private val underlying: Date) {

  private def updatedRaw(modifier: Double => Double): JSDate =
    new JSDate(new Date(modifier(underlying.getTime())))

  private def updated(modifier: Date => Unit): JSDate = {
    val date = new Date(underlying.getTime())
    modifier(date)
    new JSDate(date)
  }

  def dayOfMonth: Int = underlying.getUTCDate()
  def month: Int = underlying.getUTCMonth() + 1
  def year: Int = underlying.getUTCFullYear()
  def hour: Int = underlying.getUTCHours()
  def minute: Int = underlying.getUTCMinutes()
  def second: Int = underlying.getUTCSeconds()
  def millisecond: Int = underlying.getUTCMilliseconds()

  def toLocalDate: LocalDate = LocalDate.of(year, month, dayOfMonth)
  def toLocalTime: LocalTime = LocalTime.of(hour, minute, second, millisToNanos(millisecond))

  def jsGetTime: Double = underlying.getTime()

  def withYear(year: Int): JSDate = updated(_.setUTCFullYear(year, month, dayOfMonth))
  def withMonth(month: Int): JSDate = updated(_.setUTCMonth(month - 1, dayOfMonth))
  def withDayOfMonth(dayOfMonth: Int): JSDate = updated(_.setUTCDate(dayOfMonth))
  def withHour(hour: Int): JSDate = updated(_.setUTCHours(hour, minute, second, millisecond))
  def withMinute(minute: Int): JSDate = updated(_.setUTCMinutes(minute, second, millisecond))
  def withSecond(second: Int): JSDate = updated(_.setUTCSeconds(second, millisecond))
  def withMillisecond(millisecond: Int): JSDate = updated(_.setUTCMilliseconds(millisecond))

  def millisecondsUntil(other: JSDate): Long = (other.jsGetTime - jsGetTime).toLong
  def secondsUntil(other: JSDate): Long = millisecondsUntil(other) / MillisInSecond
  def minutesUntil(other: JSDate): Long = millisecondsUntil(other) / MillisInMinute
  def hoursUntil(other: JSDate): Long = millisecondsUntil(other) / MillisInHour

  def plus(d: Duration): JSDate = plusMillis(d.toMillis)
  def plusMillis(n: Long): JSDate = updatedRaw(_ + n)

  override def toString = underlying.toUTCString()
}

object JSDate {

  def now: JSDate = new JSDate(new Date())

  def compare(x: JSDate, y: JSDate) = Ordering.Double.compare(x.underlying.getTime(), y.underlying.getTime())

  def of(year: Int, month: Int, day: Int, hour: Int, minute: Int = 0, second: Int = 0, millisecond: Int = 0): JSDate = {
    val date = Try(LocalDate.of(year, month, day))
    require(date.isSuccess, s"Invalid date: ${date.failed.get.getMessage}")

    val time = Try(LocalTime.of(hour, minute, second, millisToNanos(millisecond)))
    require(time.isSuccess, s"Invalid time: ${time.failed.get.getMessage}")

    of(date.get, time.get)
  }


  def of(date: LocalDate, time: LocalTime): JSDate = new JSDate(new Date(Date.UTC(
    date.getYear, date.getMonthValue - 1, date.getDayOfMonth,
    time.getHour, time.getMinute, time.getSecond, time.get(ChronoField.MILLI_OF_SECOND)
  )))
}
