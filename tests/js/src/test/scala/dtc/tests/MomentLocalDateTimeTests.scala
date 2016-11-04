package dtc.tests

import java.time.{LocalDate, LocalTime}

import cats.kernel.laws.OrderLaws
import dtc.instances.moment._
import dtc.js.MomentLocalDateTime
import dtc.laws.{DateTimeTCTests, LocalDateTimeTCTests}
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary

class MomentLocalDateTimeTests extends DTCSuiteJS {

  implicit val arbT: Arbitrary[MomentLocalDateTime] = Arbitrary(for {
    date <- arbitrary[LocalDate]
    time <- arbitrary[LocalTime]
  } yield MomentLocalDateTime.of(date, time))

  implicit val cogenT = cogenMomentDateTime[MomentLocalDateTime]

  val pairGen = overflowSafePairGen.map(t => (MomentLocalDateTime.of(t._1, t._2), t._3))
  val ldtTests = LocalDateTimeTCTests[MomentLocalDateTime](
    pairGen,
    genJSValidYear
  )
  checkAll("MomentLocalDateTimeTests", DateTimeTCTests[MomentLocalDateTime](pairGen).dateTime)
  checkAll("MomentLocalDateTimeTests", ldtTests.localDateTime)
  // see: https://github.com/moment/moment/issues/3029
  // checkAll("MomentLocalDateTimeTests", ldtTests.localDateTime)
  checkAll("MomentLocalDateTimeTests", OrderLaws[MomentLocalDateTime].order)
  checkAll("MomentLocalDateTimeTests", OrderLaws[MomentLocalDateTime].partialOrder)
  checkAll("MomentLocalDateTimeTests", OrderLaws[MomentLocalDateTime].eqv)

}

