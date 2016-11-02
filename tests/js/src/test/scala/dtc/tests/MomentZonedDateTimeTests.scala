package dtc.tests

import java.time.{LocalDate, LocalTime}

import cats.kernel.laws.OrderLaws
import dtc.TimeZoneId
import dtc.instances.moment._
import dtc.js.MomentZonedDateTime
import dtc.laws.DateTimeTCTests
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

class MomentZonedDateTimeTests extends DTCSuiteJS {

  implicit val arbT: Arbitrary[MomentZonedDateTime] = Arbitrary(for {
    date <- arbitrary[LocalDate]
    time <- arbitrary[LocalTime]
    zone <- Gen.oneOf(availableZoneIds).map(TimeZoneId)
  } yield MomentZonedDateTime.of(date, time, zone))

  implicit val cogenT = cogenMomentDateTime[MomentZonedDateTime]

  checkAll("MomentZonedDateTime", DateTimeTCTests[MomentZonedDateTime].dateTime)
  checkAll("MomentZonedDateTime", OrderLaws[MomentZonedDateTime].order)
  checkAll("MomentZonedDateTime", OrderLaws[MomentZonedDateTime].partialOrder)
  checkAll("MomentZonedDateTime", OrderLaws[MomentZonedDateTime].eqv)
}

