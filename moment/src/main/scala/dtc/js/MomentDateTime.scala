package dtc.js

import java.time.{Duration, LocalDate, LocalTime}

import org.widok.moment.{Date, Moment}

trait MomentDateTime[T <: MomentDateTime[T]] {

  protected val underlying: Date

  protected def copy = Moment(underlying)

  def jsGetTime = underlying.value()

  def dayOfMonth: Int = underlying.date()
  def month: Int = underlying.month() + 1
  def year: Int = underlying.year()
  def hour: Int = underlying.hour()
  def minute: Int = underlying.minute()
  def second: Int = underlying.second()
  def millisecond: Int = underlying.millisecond()

  def toLocalDate: LocalDate = LocalDate.of(year, month, dayOfMonth)
  def toLocalTime: LocalTime = LocalTime.of(hour, minute, second, millisecond * 1000000)

  def plus(d: Duration): T = plusMillis(d.toMillis)
  def plusMillis(n: Long): T

  override def toString: String = underlying.toString
}

object MomentDateTime {

  def compare[T <: MomentDateTime[T]](x: T, y: T) =
    Ordering.Double.compare(x.underlying.value(), y.underlying.value())
}
