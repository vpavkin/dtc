package dtc.instances

import java.time._

import dtc._

object zonedDateTime {

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

  implicit val zonedDateTimeWithStrictEquality: Zoned[ZonedDateTime] =
    new ZonedDateTimeInstanceWithoutOrder {
      def compare(x: ZonedDateTime, y: ZonedDateTime): Int = x.compareTo(y)
    }
}
