package dtc.tests

import java.time.{LocalDate, LocalTime}

import cats.kernel.laws.OrderLaws
import dtc.instances.moment._
import dtc.js.MomentLocalDateTime
import dtc.laws.{DateTimeTests, LocalDateTimeTests, ProviderTests}
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import dtc.instances.moment.providers.realMomentLocalDateTimeProvider

class MomentLocalDateTimeTests extends DTCSuiteJS {

  implicit val arbT: Arbitrary[MomentLocalDateTime] = Arbitrary(for {
    date <- arbitrary[LocalDate]
    time <- arbitrary[LocalTime]
  } yield MomentLocalDateTime.of(date, time))

  implicit val cogenT = cogenMomentDateTime[MomentLocalDateTime]

  val pairGen = overflowSafePairGen.map(t => (MomentLocalDateTime.of(t._1, t._2), t._3))
  val ldtTests = LocalDateTimeTests[MomentLocalDateTime](
    pairGen,
    genJSValidYear
  )
  checkAll("MomentLocalDateTimeTests", DateTimeTests[MomentLocalDateTime](pairGen).dateTime)
  checkAll("MomentLocalDateTimeTests", ldtTests.localDateTime)
  // see: https://github.com/moment/moment/issues/3029
  // checkAll("MomentLocalDateTimeTests", ldtTests.localDateTime)
  checkAll("MomentLocalDateTimeTests", OrderLaws[MomentLocalDateTime].order)
  checkAll("MomentLocalDateTimeTests", OrderLaws[MomentLocalDateTime].partialOrder)
  checkAll("MomentLocalDateTimeTests", OrderLaws[MomentLocalDateTime].eqv)

  checkAll("MomentLocalDateTimeTests", ProviderTests[MomentLocalDateTime](genTimeZone).provider)
}

