package dtc.tests

import java.time.{LocalDate, LocalTime}

import dtc.instances.moment._
import dtc.js.MomentLocalDateTime
import dtc.laws.{DateTimeTCTests, LocalDateTimeTCTests, OrderLaws}
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary

class MomentLocalDateTimeTests extends DTCSuiteJS {

  implicit val arbT: Arbitrary[MomentLocalDateTime] = Arbitrary(for {
    date <- arbitrary[LocalDate]
    time <- arbitrary[LocalTime]
  } yield MomentLocalDateTime.of(date, time))

  implicit val cogenT = cogenMomentDateTime[MomentLocalDateTime]

  checkAll("MomentLocalDateTimeTests", DateTimeTCTests[MomentLocalDateTime].dateTime)
  checkAll("MomentLocalDateTimeTests", LocalDateTimeTCTests[MomentLocalDateTime](
    overflowSafePairGen.map(t => (MomentLocalDateTime.of(t._1, t._2), t._3)),
    genJSValidYear
  ).localDateTime)
  checkAll("MomentLocalDateTimeTests", OrderLaws[MomentLocalDateTime].order)
  checkAll("MomentLocalDateTimeTests", OrderLaws[MomentLocalDateTime].partialOrder)
  checkAll("MomentLocalDateTimeTests", OrderLaws[MomentLocalDateTime].eqv)

}

