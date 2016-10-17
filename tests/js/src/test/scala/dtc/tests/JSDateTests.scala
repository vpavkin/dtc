package dtc.tests

import java.time.{LocalDate, LocalTime}

import dtc.instances.jsDate._
import dtc.js.JSDate
import dtc.laws.{DateTimeTCTests, LocalDateTimeTCTests, OrderLaws}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Cogen}

class JSDateTests extends ExtendedSyntaxTests[JSDate] with DTCSuiteJS {

  implicit val cogenT: Cogen[JSDate] = Cogen(_.jsGetTime.toLong)

  implicit val arbT: Arbitrary[JSDate] = Arbitrary(for {
    date <- arbitrary[LocalDate]
    time <- arbitrary[LocalTime]
  } yield JSDate.of(date, time))


  checkAll("JSDate", LocalDateTimeTCTests[JSDate](
    overflowSafePairGen.map(t => (JSDate.of(t._1, t._2), t._3)), genJSValidYear
  ).localDateTime)
  checkAll("JSDate", DateTimeTCTests[JSDate].dateTime)
  checkAll("JSDate", OrderLaws[JSDate].order)
  checkAll("JSDate", OrderLaws[JSDate].partialOrder)
  checkAll("JSDate", OrderLaws[JSDate].eqv)
}

