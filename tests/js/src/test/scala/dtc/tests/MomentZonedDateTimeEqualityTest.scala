package dtc.tests

import dtc.TimeZoneId
import dtc.js.MomentZonedDateTime
import moment.Moment
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuiteLike

class MomentZonedDateTimeEqualityTest extends AnyFunSuiteLike with Matchers {

  private val instant = 1431231233567.0
  private val x = MomentZonedDateTime.of(Moment(instant).tz("Canada/Pacific"), TimeZoneId("Canada/Pacific"))
  private val y = MomentZonedDateTime.of(Moment(instant).tz("America/Vancouver"), TimeZoneId("America/Vancouver"))

  test("strict equality") {
    import dtc.instances.moment.momentZonedWithStrictEquality
    momentZonedWithStrictEquality.eqv(x, y) shouldBe false
  }

  test("cross-zone equality") {
    import dtc.instances.moment.momentZonedWithCrossZoneEquality
    momentZonedWithCrossZoneEquality.eqv(x, y) shouldBe true
  }
}

