package dtc.tests

import java.time.{LocalDate, LocalTime}

import dtc.TimeZoneId
import dtc.instances.moment._
import dtc.js.MomentDateTime
import dtc.laws.{DateTimeTCTests, OrderLaws}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Cogen, Gen}

class MomentDateTimeTests extends ExtendedSyntaxTests[MomentDateTime] {

  val anchorDate = LocalDate.of(1970, 1, 1)

  // see http://ecma-international.org/ecma-262/5.1/#sec-15.9.1.1
  override implicit val arbLocalDate: Arbitrary[LocalDate] = Arbitrary(
    Gen.choose(-100000000L, 100000000L).map(anchorDate.plusDays)
  )

  implicit val cogenT: Cogen[MomentDateTime] = Cogen(_.jsGetTime.toLong)

  implicit val arbT: Arbitrary[MomentDateTime] = Arbitrary(for {
    date <- arbitrary[LocalDate]
    time <- arbitrary[LocalTime]
    zone <- Gen.oneOf(availableZoneIds).map(TimeZoneId)
  } yield MomentDateTime.of(date, time, zone))


  checkAll("JSDate", DateTimeTCTests[MomentDateTime].dateTime)
  checkAll("JSDate", OrderLaws[MomentDateTime].order)
  checkAll("JSDate", OrderLaws[MomentDateTime].partialOrder)
  checkAll("JSDate", OrderLaws[MomentDateTime].eqv)
}

