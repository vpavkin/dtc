package dtc.tests

import java.time.ZonedDateTime

import dtc.instances.zonedDateTime._
import dtc.laws.{DateTimeTCTests, OrderLaws}
import org.scalacheck.{Arbitrary, Cogen}

class ZonedDateTimeTests extends DTCSuiteJVM {

  implicit val arbT: Arbitrary[ZonedDateTime] = com.fortysevendeg.scalacheck.datetime.jdk8.ArbitraryJdk8.arbJdk8
  implicit val cogenT: Cogen[ZonedDateTime] = Cogen(_.toEpochSecond)

  checkAll("java.time.ZonedDateTime", DateTimeTCTests[ZonedDateTime].dateTime)
  checkAll("java.time.ZonedDateTime", OrderLaws[ZonedDateTime].order)
  checkAll("java.time.ZonedDateTime", OrderLaws[ZonedDateTime].partialOrder)
  checkAll("java.time.ZonedDateTime", OrderLaws[ZonedDateTime].eqv)
}

