package dtc.js

import java.time.{LocalDate, LocalTime}
import dtc._
import org.widok.moment.{Date, Moment, Units}

import scala.util.Try

/**
  * Mutability safe wrapper around moment Date.
  */
class MomentLocalDateTime private(protected override val underlying: Date)
  extends MomentDateTime[MomentLocalDateTime] {

  require(underlying.isValid())

  def plusMillis(n: Long): MomentLocalDateTime = updated(_.add(n.toDouble, Units.Millisecond))

  // todo: move up to base trait after https://github.com/widok/scala-js-momentjs/pull/20 is released
  def withYear(year: Int): MomentLocalDateTime = MomentLocalDateTime.of(toLocalDate.withYear(year), toLocalTime)
  def withMonth(month: Int): MomentLocalDateTime =
    MomentLocalDateTime.of(toLocalDate.withMonth(month), toLocalTime)
  def withDayOfMonth(dayOfMonth: Int): MomentLocalDateTime =
    MomentLocalDateTime.of(toLocalDate.withDayOfMonth(dayOfMonth), toLocalTime)
  def withHour(hour: Int): MomentLocalDateTime =
    MomentLocalDateTime.of(toLocalDate, toLocalTime.withHour(hour))
  def withMinute(minute: Int): MomentLocalDateTime =
    MomentLocalDateTime.of(toLocalDate, toLocalTime.withMinute(minute))
  def withSecond(second: Int): MomentLocalDateTime =
    MomentLocalDateTime.of(toLocalDate, toLocalTime.withSecond(second))
  def withMillisecond(millisecond: Int): MomentLocalDateTime =
    MomentLocalDateTime.of(toLocalDate, toLocalTime.withNano(millisToNanos(millisecond)))

  protected def updated(modifier: Date => Date): MomentLocalDateTime =
    new MomentLocalDateTime(modifier(copy))
}

object MomentLocalDateTime {

  def now: MomentLocalDateTime = {
    val local = Moment()
    val offset = local.utcOffset()
    new MomentLocalDateTime(local.utc().add(offset, Units.Minute))
  }

  def of(
    year: Int, month: Int, day: Int,
    hour: Int, minute: Int = 0, second: Int = 0, millisecond: Int = 0): MomentLocalDateTime = {
    val date = Try(LocalDate.of(year, month, day))
    require(date.isSuccess, s"Invalid date: ${date.failed.get.getMessage}")

    val time = Try(LocalTime.of(hour, minute, second, millisToNanos(millisecond)))
    require(time.isSuccess, s"Invalid time: ${time.failed.get.getMessage}")

    of(date.get, time.get)
  }

  def of(date: LocalDate, time: LocalTime): MomentLocalDateTime =
    new MomentLocalDateTime(utcMoment(date, time))
}
