package dtc.tests

import java.time.{Instant, ZoneId, ZonedDateTime}

import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuiteLike

class ZonedDateTimeEqualityTest extends AnyFunSuiteLike with Matchers {

  private val instant = 1431231233567L
  private val x = ZonedDateTime.ofInstant(Instant.ofEpochMilli(instant), ZoneId.of("Canada/Pacific"))
  private val y = ZonedDateTime.ofInstant(Instant.ofEpochMilli(instant), ZoneId.of("America/Vancouver"))

  test("strict equality") {
    dtc.instances.zonedDateTime.zonedDateTimeWithStrictEquality.eqv(x, y) shouldBe false
  }

  test("cross-zone equality") {
    dtc.instances.zonedDateTime.zonedDateTimeWithCrossZoneEquality.eqv(x, y) shouldBe true
  }
}

