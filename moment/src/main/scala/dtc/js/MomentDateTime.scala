package dtc.js

import java.time.{Duration, LocalDate, LocalTime}

import dtc._
import org.widok.moment.{Date, Moment, Units}

trait MomentDateTime[T <: MomentDateTime[T]] {

  protected val underlying: Date

  protected def copy = Moment(underlying)

  protected def updated(modifier: Date => Date): T

  def jsGetTime = underlying.value()

  def dayOfMonth: Int = underlying.date()
  def month: Int = underlying.month() + 1
  def year: Int = underlying.year()
  def hour: Int = underlying.hour()
  def minute: Int = underlying.minute()
  def second: Int = underlying.second()
  def millisecond: Int = underlying.millisecond()

  def toLocalDate: LocalDate = LocalDate.of(year, month, dayOfMonth)
  def toLocalTime: LocalTime = LocalTime.of(hour, minute, second, millisToNanos(millisecond))

  def millisecondsUntil(other: T): Long = underlying.diff(other.underlying, Units.Millisecond).toLong
  def secondsUntil(other: T): Long = underlying.diff(other.underlying, Units.Second).toLong
  def minutesUntil(other: T): Long = underlying.diff(other.underlying, Units.Minute).toLong
  def hoursUntil(other: T): Long = underlying.diff(other.underlying, Units.Hour).toLong

  def plus(d: Duration): T = plusMillis(d.toMillis)
  def plusMillis(n: Long): T

  override def toString: String = underlying.toString
}

object MomentDateTime {

  def compare[T <: MomentDateTime[T]](x: T, y: T) =
    Ordering.Double.compare(x.underlying.value(), y.underlying.value())
}
