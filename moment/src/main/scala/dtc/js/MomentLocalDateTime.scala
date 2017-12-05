package dtc.js

import java.time.{LocalDate, LocalTime}

import dtc._
import dtc.js.MomentDateTime.utcMoment
import moment.{Date, Moment, Units}

import scala.util.Try

/**
  * Mutability safe wrapper around momentjs Date.
  *
  * Performs all operations in UTC mode, effectively providing a local date time value.
  *
  * Wrapper also overrides semantics to JVM-identical.
  */
class MomentLocalDateTime private(protected override val underlying: Date)
  extends MomentDateTime[MomentLocalDateTime] {

  require(underlying.isValid())

  def plusMillis(n: Long): MomentLocalDateTime = updated(_.add(n.toDouble, Units.Millisecond))

  protected def updated(modifier: Date => Date): MomentLocalDateTime =
    new MomentLocalDateTime(modifier(copy))

  override def equals(obj: scala.Any): Boolean = obj match {
    case m: MomentLocalDateTime => MomentDateTime.compare(this, m) == 0
    case _ => false
  }

  override def hashCode() = underlying.value().toInt
}

object MomentLocalDateTime {

  def now(timeZoneId: TimeZoneId): MomentLocalDateTime = {
    val zoned = Moment().tz(timeZoneId.id)
    val offset = zoned.utcOffset()
    new MomentLocalDateTime(zoned.utc().add(offset, Units.Minute))
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
