package dtc

import cats.kernel.Eq

/**
  * Cross-platform representation for zoned time offset
  *
  * @param seconds difference from UTC time in seconds
  */
case class Offset(seconds: Int)

object Offset {
  implicit val eqInstance: Eq[Offset] = Eq.fromUniversalEquals
}
