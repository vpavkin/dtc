package dtc.tests

import java.time.{LocalDate, LocalTime}

import cats.kernel.laws.OrderLaws
import dtc.TimeZoneId
import dtc.instances.moment._
import dtc.js.MomentZonedDateTime
import dtc.laws.DateTimeTCTests
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary

class MomentZonedDateTimeTests extends DTCSuiteJS {

  implicit val arbT: Arbitrary[MomentZonedDateTime] = Arbitrary(for {
    date <- arbitrary[LocalDate]
    time <- arbitrary[LocalTime]
    zone <- arbitrary[TimeZoneId]
  } yield MomentZonedDateTime.of(date, time, zone))

  implicit val cogenT = cogenMomentDateTime[MomentZonedDateTime]

  val pairGen = for {
    zone <- arbitrary[TimeZoneId]
    pair <- overflowSafePairGen
  } yield (MomentZonedDateTime.of(pair._1, pair._2, zone), pair._3)

  checkAll("MomentZonedDateTime", DateTimeTCTests[MomentZonedDateTime](pairGen).dateTime)
  checkAll("MomentZonedDateTime", OrderLaws[MomentZonedDateTime].order)
  checkAll("MomentZonedDateTime", OrderLaws[MomentZonedDateTime].partialOrder)
  checkAll("MomentZonedDateTime", OrderLaws[MomentZonedDateTime].eqv)
}

