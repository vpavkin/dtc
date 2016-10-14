package dtc.tests

import java.time.{LocalDate, LocalTime}

import dtc.instances.moment._
import dtc.js.MomentLocalDateTime
import dtc.laws.{DateTimeTCTests, OrderLaws}
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary

class MomentLocalDateTimeTests extends ExtendedSyntaxTests[MomentLocalDateTime] {

  implicit val arbT: Arbitrary[MomentLocalDateTime] = Arbitrary(for {
    date <- arbitrary[LocalDate]
    time <- arbitrary[LocalTime]
  } yield MomentLocalDateTime.of(date, time))

  override implicit val arbLocalDate: Arbitrary[LocalDate] = arbJSLocalDate
  implicit val cogenT = cogenMomentDateTime[MomentLocalDateTime]

  checkAll("MomentLocalDateTimeTests", DateTimeTCTests[MomentLocalDateTime].dateTime)
  checkAll("MomentLocalDateTimeTests", OrderLaws[MomentLocalDateTime].order)
  checkAll("MomentLocalDateTimeTests", OrderLaws[MomentLocalDateTime].partialOrder)
  checkAll("MomentLocalDateTimeTests", OrderLaws[MomentLocalDateTime].eqv)

}

