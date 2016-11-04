package dtc.tests

import java.time.{Duration, ZonedDateTime}

import cats.kernel.laws.OrderLaws
import dtc.instances.zonedDateTime._
import dtc.laws.DateTimeTCTests
import org.scalacheck.{Arbitrary, Cogen}
import org.scalacheck.Arbitrary.arbitrary

class ZonedDateTimeTests extends DTCSuiteJVM {

  implicit val arbT: Arbitrary[ZonedDateTime] = com.fortysevendeg.scalacheck.datetime.jdk8.ArbitraryJdk8.arbJdk8
  implicit val cogenT: Cogen[ZonedDateTime] = Cogen(_.toEpochSecond)

  val overflowSafePairGen = for {
    dt <- arbitrary[ZonedDateTime]
    dur <- arbitrary[Duration]
  } yield (dt, dur)

  checkAll("java.time.ZonedDateTime", DateTimeTCTests[ZonedDateTime](overflowSafePairGen).dateTime)
  checkAll("java.time.ZonedDateTime", OrderLaws[ZonedDateTime].order)
  checkAll("java.time.ZonedDateTime", OrderLaws[ZonedDateTime].partialOrder)
  checkAll("java.time.ZonedDateTime", OrderLaws[ZonedDateTime].eqv)
}

