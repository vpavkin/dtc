package dtc.js

import java.time.temporal.{ChronoField, ChronoUnit}
import java.time.{DayOfWeek, Duration, LocalDate, LocalTime}

import dtc._

import scala.scalajs.js.Date
import scala.util.Try

/**
  * Mutability safe wrapper around plain JS Date.
  *
  * Shouldn't be used as a standalone thing as API is just enough
  * to fit [[dtc.Local]] typeclass requirements.
  *
  * Supports only [[dtc.Local]] typeclass due to weak time-zone capabilities.
  */
class JSDate private(private val underlying: Date) {

  private def updatedRaw(modifier: Double => Double): JSDate =
    new JSDate(new Date(modifier(underlying.getTime())))

  private def updated(modifier: Date => Unit): JSDate = {
    val date = new Date(underlying.getTime())
    modifier(date)
    new JSDate(date)
  }

  private def limitToLastDayOfMonth(day: Int, forYear: Int = year, forMonth: Int = month) =
    math.min(day, LocalDate.of(forYear, forMonth, 1).lengthOfMonth())

  def dayOfMonth: Int = underlying.getUTCDate()
  def month: Int = underlying.getUTCMonth() + 1
  def year: Int = underlying.getUTCFullYear()
  def hour: Int = underlying.getUTCHours()
  def minute: Int = underlying.getUTCMinutes()
  def second: Int = underlying.getUTCSeconds()
  def millisecond: Int = underlying.getUTCMilliseconds()
  def dayOfWeek: DayOfWeek = DayOfWeek.of(dayOfWeekJSToJVM(underlying.getUTCDay()))
  def toLocalDate: LocalDate = LocalDate.of(year, month, dayOfMonth)
  def toLocalTime: LocalTime = LocalTime.of(hour, minute, second, millisToNanos(millisecond))

  def jsGetTime: Double = underlying.getTime()

  def withYear(year: Int): JSDate = updated(_.setUTCFullYear(year, month - 1, limitToLastDayOfMonth(dayOfMonth, year)))
  def withMonth(month: Int): JSDate =
    updated(_.setUTCMonth(month - 1, limitToLastDayOfMonth(dayOfMonth, forMonth = month)))
  def withDayOfMonth(dayOfMonth: Int): JSDate = updated(_.setUTCDate(dayOfMonth))
  def withHour(hour: Int): JSDate = updated(_.setUTCHours(hour, minute, second, millisecond))
  def withMinute(minute: Int): JSDate = updated(_.setUTCMinutes(minute, second, millisecond))
  def withSecond(second: Int): JSDate = updated(_.setUTCSeconds(second, millisecond))
  def withMillisecond(millisecond: Int): JSDate = updated(_.setUTCMilliseconds(millisecond))

  def millisecondsUntil(other: JSDate): Long = (other.jsGetTime - jsGetTime).toLong
  def secondsUntil(other: JSDate): Long = millisecondsUntil(other) / MillisInSecond
  def minutesUntil(other: JSDate): Long = millisecondsUntil(other) / MillisInMinute
  def hoursUntil(other: JSDate): Long = millisecondsUntil(other) / MillisInHour
  def daysUntil(other: JSDate): Long = millisecondsUntil(other) / MillisInDay
  def monthsUntil(other: JSDate): Long = monthsOrYearsUntil(other, ChronoUnit.MONTHS)
  def yearsUntil(other: JSDate): Long = monthsOrYearsUntil(other, ChronoUnit.YEARS)

  private def monthsOrYearsUntil(other: JSDate, units: ChronoUnit): Long = {
    val thisDate = toLocalDate
    val thisTime = toLocalTime
    val otherDate = other.toLocalDate
    val otherTime = other.toLocalTime

    if (otherDate.isAfter(thisDate) && otherTime.isBefore(thisTime)) thisDate.until(otherDate.minusDays(1L), units)
    else if (otherDate.isBefore(thisDate) && otherTime.isAfter(thisTime)) thisDate.until(otherDate.plusDays(1L), units)
    else thisDate.until(otherDate, units)
  }

  def plus(d: Duration): JSDate = plusMillis(d.toMillis)
  def minus(d: Duration): JSDate = plusMillis(-d.toMillis)

  def plusMonths(n: Int): JSDate = JSDate.of(toLocalDate.plusMonths(n.toLong), toLocalTime)
  def plusYears(n: Int): JSDate = {
    val newYear = year + n
    updated(_.setUTCFullYear(
      newYear,
      underlying.getUTCMonth(),
      limitToLastDayOfMonth(dayOfMonth, forYear = newYear)
    ))
  }
  def plusMillis(n: Long): JSDate = updatedRaw(_ + n)

  override def toString: String = underlying.toUTCString()
}

object JSDate {

  def now: JSDate = new JSDate(new Date())

  def compare(x: JSDate, y: JSDate): Int = Ordering.Double.compare(x.underlying.getTime(), y.underlying.getTime())

  def of(year: Int, month: Int, day: Int, hour: Int, minute: Int = 0, second: Int = 0, millisecond: Int = 0): JSDate = {
    val date = Try(LocalDate.of(year, month, day))
    require(date.isSuccess, s"Invalid date: ${date.failed.get.getMessage}")

    val time = Try(LocalTime.of(hour, minute, second, millisToNanos(millisecond)))
    require(time.isSuccess, s"Invalid time: ${time.failed.get.getMessage}")

    of(date.get, time.get)
  }


  def of(date: LocalDate, time: LocalTime): JSDate = {
    val jsDate = new Date(Date.UTC(
      date.getYear, date.getMonthValue - 1, date.getDayOfMonth,
      time.getHour, time.getMinute, time.getSecond, time.get(ChronoField.MILLI_OF_SECOND)
    ))

    // scalastyle:off
    // see: https://developer.mozilla.org/en/docs/Web/JavaScript/Reference/Global_Objects/Date#Two_digit_years_map_to_1900_-_1999
    // scalastyle:on
    if (date.getYear >= 0 && date.getYear <= 99)
      jsDate.setUTCFullYear(date.getYear, date.getMonthValue - 1, date.getDayOfMonth)
    new JSDate(jsDate)
  }
}
