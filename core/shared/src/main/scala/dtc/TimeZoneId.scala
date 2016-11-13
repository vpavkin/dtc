package dtc

import cats.kernel.Eq
/**
  * Cross-platform wrapper for time-zone id values.
  *
  * @param id raw zone id value
  */
case class TimeZoneId(id: String) extends AnyVal

object TimeZoneId {
  implicit val eqInstance: Eq[TimeZoneId] = Eq.instance(_.id == _.id)
}
