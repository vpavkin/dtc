package dtc.tests

import java.time.{LocalDate, LocalTime}

import dtc.instances.jsDate._
import dtc.js.JSDate
import dtc.laws.{LocalDateTimeTCTests, OrderLaws}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Cogen, Gen}

class JSDateTests extends ExtendedSyntaxTests[JSDate] {

  val anchorDate = LocalDate.of(1970, 1, 1)

  // see http://ecma-international.org/ecma-262/5.1/#sec-15.9.1.1
  override implicit val arbLocalDate: Arbitrary[LocalDate] = Arbitrary(
    Gen.choose(-100000000L, 100000000L).map(anchorDate.plusDays)
  )

  implicit val cogenT: Cogen[JSDate] = Cogen(_.jsGetTime.toLong)

  implicit val arbT: Arbitrary[JSDate] = Arbitrary(for {
    date <- arbitrary[LocalDate]
    time <- arbitrary[LocalTime]
  } yield JSDate.of(date, time))


  checkAll("JSDate", LocalDateTimeTCTests[JSDate].localDateTime)
  checkAll("JSDate", OrderLaws[JSDate].order)
  checkAll("JSDate", OrderLaws[JSDate].partialOrder)
  checkAll("JSDate", OrderLaws[JSDate].eqv)
}

