package dtc.instances

import java.time._

import dtc._

object zonedDateTime {

  /**
    * This instance uses relaxed time value equality, as described in `ZonedDateTime#isEqual` method.
    *
    * Time values are equal when underlying instants are equal. Even if they have different time zones.
    */
  implicit val zonedDateTimeWithCrossZoneEquality: Zoned[ZonedDateTime] =
    new ZonedDateTimeInstanceWithoutOrder {
      def compare(x: ZonedDateTime, y: ZonedDateTime): Int =
        if (lt(x, y)) -1
        else if (gt(x, y)) 1
        else 0

      override def eqv(x: ZonedDateTime, y: ZonedDateTime): Boolean =
        x.isEqual(y)

      override def neqv(x: ZonedDateTime, y: ZonedDateTime): Boolean =
        !x.isEqual(y)

      override def lteqv(x: ZonedDateTime, y: ZonedDateTime): Boolean =
        x.isBefore(y) || eqv(x, y)

      override def lt(x: ZonedDateTime, y: ZonedDateTime): Boolean =
        x.isBefore(y)

      override def gteqv(x: ZonedDateTime, y: ZonedDateTime): Boolean =
        x.isAfter(y) || eqv(x, y)

      override def gt(x: ZonedDateTime, y: ZonedDateTime): Boolean =
        x.isAfter(y)
    }

  /**
    * This instance uses strict time value equality, as described in `ZonedDateTime#compareTo` method.
    *
    * Time values with different time zones are always considered different,
    * regardless of underlying instant equivalence.
    */
  implicit val zonedDateTimeWithStrictEquality: Zoned[ZonedDateTime] =
    new ZonedDateTimeInstanceWithoutOrder {
      def compare(x: ZonedDateTime, y: ZonedDateTime): Int = x.compareTo(y)
    }
}
