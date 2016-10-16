package dtc.js

import java.time.temporal.ChronoField
import java.time.{Duration, LocalDate, LocalTime}

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

  private def updated(modifier: Double => Double): JSDate =
    new JSDate(new Date(modifier(underlying.getTime())))

  def dayOfMonth: Int = underlying.getUTCDate()
  def month: Int = underlying.getUTCMonth() + 1
  def year: Int = underlying.getUTCFullYear()
  def hour: Int = underlying.getUTCHours()
  def minute: Int = underlying.getUTCMinutes()
  def second: Int = underlying.getUTCSeconds()
  def millisecond: Int = underlying.getUTCMilliseconds()

  def toLocalDate: LocalDate = LocalDate.of(year, month, dayOfMonth)
  def toLocalTime: LocalTime = LocalTime.of(hour, minute, second, millisecond * 1000000)

  def jsGetTime: Double = underlying.getTime()

  def plus(d: Duration): JSDate = plusMillis(d.toMillis)
  def plusMillis(n: Long): JSDate = updated(_ + n)

  override def toString = underlying.toUTCString()
}

object JSDate {

  def now: JSDate = of(LocalDate.now(), LocalTime.now())

  def compare(x: JSDate, y: JSDate) = Ordering.Double.compare(x.underlying.getTime(), y.underlying.getTime())

  def of(year: Int, month: Int, day: Int, hour: Int, minute: Int = 0, second: Int = 0, millisecond: Int = 0): JSDate = {
    val date = Try(LocalDate.of(year, month, day))
    require(date.isSuccess, s"Invalid date: ${date.failed.get.getMessage}")

    val time = Try(LocalTime.of(hour, minute, second, millisecond * 1000000))
    require(time.isSuccess, s"Invalid time: ${time.failed.get.getMessage}")

    of(date.get, time.get)
  }


  def of(date: LocalDate, time: LocalTime): JSDate = new JSDate(new Date(Date.UTC(
    date.getYear, date.getMonthValue - 1, date.getDayOfMonth,
    time.getHour, time.getMinute, time.getSecond, time.get(ChronoField.MILLI_OF_SECOND)
  )))
}