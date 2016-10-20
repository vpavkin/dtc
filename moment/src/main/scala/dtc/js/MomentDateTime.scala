package dtc.js

import java.time.temporal.ChronoField
import java.time.{DayOfWeek, Duration, LocalDate, LocalTime}

import dtc._
import moment.{Date, Moment, Units}

import scala.scalajs.js.Array

trait MomentDateTime[T <: MomentDateTime[T]] {

  protected val underlying: Date

  protected def copy = Moment(underlying)

  protected def updated(modifier: Date => Date): T

  def jsGetTime = underlying.value()

  def dayOfWeek: DayOfWeek = DayOfWeek.of(dayOfWeekJSToJVM(underlying.day()))
  def dayOfMonth: Int = underlying.date()
  def month: Int = underlying.month() + 1
  def year: Int = underlying.year()
  def hour: Int = underlying.hour()
  def minute: Int = underlying.minute()
  def second: Int = underlying.second()
  def millisecond: Int = underlying.millisecond()

  def withYear(year: Int): T = updated(_.year(year.toDouble))
  def withMonth(month: Int): T = updated(_.month(month.toDouble - 1))
  def withDayOfMonth(dayOfMonth: Int): T = updated(_.date(dayOfMonth.toDouble))
  def withHour(hour: Int): T = updated(_.hour(hour.toDouble))
  def withMinute(minute: Int): T = updated(_.minute(minute.toDouble))
  def withSecond(second: Int): T = updated(_.second(second.toDouble))
  def withMillisecond(millisecond: Int): T = updated(_.millisecond(millisecond.toDouble))

  def toLocalDate: LocalDate = LocalDate.of(year, month, dayOfMonth)
  def toLocalTime: LocalTime = LocalTime.of(hour, minute, second, millisToNanos(millisecond))

  def yearsUntil(other: T): Long = -underlying.diff(other.underlying, Units.Year).toLong
  def monthsUntil(other: T): Long = -underlying.diff(other.underlying, Units.Month).toLong
  def daysUntil(other: T): Long = -underlying.diff(other.underlying, Units.Day).toLong
  def millisecondsUntil(other: T): Long = -underlying.diff(other.underlying, Units.Millisecond).toLong
  def secondsUntil(other: T): Long = -underlying.diff(other.underlying, Units.Second).toLong
  def minutesUntil(other: T): Long = -underlying.diff(other.underlying, Units.Minute).toLong
  def hoursUntil(other: T): Long = -underlying.diff(other.underlying, Units.Hour).toLong

  def plus(d: Duration): T = plusMillis(d.toMillis)
  def plusMonths(n: Int): T = updated(_.add(n.toDouble, Units.Month))
  def plusYears(n: Int): T = updated(_.add(n.toDouble, Units.Year))

  def plusMillis(n: Long): T

  override def toString: String = underlying.toString
}

object MomentDateTime {

  def compare[T <: MomentDateTime[T]](x: T, y: T) =
    Ordering.Double.compare(x.underlying.value(), y.underlying.value())

  private[js] def utcMoment(date: LocalDate, time: LocalTime): Date = Moment.utc(Array(
    date.getYear, date.getMonthValue - 1, date.getDayOfMonth,
    time.getHour, time.getMinute, time.getSecond, time.get(ChronoField.MILLI_OF_SECOND)
  ))
}
