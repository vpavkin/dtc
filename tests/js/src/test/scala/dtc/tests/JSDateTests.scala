package dtc.tests

import java.time.{LocalDate, LocalTime}

import cats.kernel.laws.OrderLaws
import dtc.instances.jsDate._
import dtc.js.JSDate
import dtc.laws.{DateTimeTests, LocalDateTimeTests}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Cogen}

class JSDateTests extends DTCSuiteJS {

  implicit val cogenT: Cogen[JSDate] = Cogen(_.jsGetTime.toLong)

  implicit val arbT: Arbitrary[JSDate] = Arbitrary(for {
    date <- arbitrary[LocalDate]
    time <- arbitrary[LocalTime]
  } yield JSDate.of(date, time))

  val pairGen = overflowSafePairGen.map(t => (JSDate.of(t._1, t._2), t._3))
  val ldtTests = LocalDateTimeTests[JSDate](
    pairGen, genJSValidYear
  )

  checkAll("JSDate", DateTimeTests[JSDate](pairGen).dateTime)
  checkAll("JSDate", ldtTests.localDateTime)
  checkAll("JSDate", ldtTests.monthUntilFractionHandling)
  checkAll("JSDate", OrderLaws[JSDate].order)
  checkAll("JSDate", OrderLaws[JSDate].partialOrder)
  checkAll("JSDate", OrderLaws[JSDate].eqv)
}

