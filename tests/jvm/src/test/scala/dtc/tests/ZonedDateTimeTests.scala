package dtc.tests

import java.time.temporal.ChronoUnit
import java.time.{Duration, ZonedDateTime}

import cats.kernel.laws.OrderLaws
import dtc.instances.zonedDateTime._
import dtc.laws.{DateTimeTCTests, ZonedDateTimeTCTests}
import dtc.syntax.timeZone._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Cogen}

class ZonedDateTimeTests extends DTCSuiteJVM {

  implicit val arbT: Arbitrary[ZonedDateTime] = com.fortysevendeg.scalacheck.datetime.jdk8.ArbitraryJdk8.arbJdk8
  implicit val cogenT: Cogen[ZonedDateTime] = Cogen(_.toEpochSecond)

  val overflowSafePairGen = for {
    dt <- arbitrary[ZonedDateTime]
    dur <- arbitrary[Duration]
  } yield (dt, dur)

  def genDateFromPeriod(period: SameZoneOffsetPeriod) =
    genDateTimeFromSameOffsetPeriod(period).map(tpl => ZonedDateTime.of(tpl._1, tpl._2, tpl._3.zoneId))

  val overflowSafePairGenWithinSameOffset = for {
    period <- arbitrary[SameZoneOffsetPeriod]
    dateTime <- genDateFromPeriod(period)
    duration <- genDateFromPeriod(period)
      .map(other => dateTime.until(other, ChronoUnit.NANOS))
      .map(Duration.ofNanos)
  } yield (dateTime, duration)

  checkAll("java.time.ZonedDateTime", DateTimeTCTests[ZonedDateTime](overflowSafePairGen).dateTime)
  checkAll("java.time.ZonedDateTime", ZonedDateTimeTCTests[ZonedDateTime](
    overflowSafePairGenWithinSameOffset,
    genYear,
    genTimeZone
  ).zonedDateTime)
  checkAll("java.time.ZonedDateTime", OrderLaws[ZonedDateTime].order)
  checkAll("java.time.ZonedDateTime", OrderLaws[ZonedDateTime].partialOrder)
  checkAll("java.time.ZonedDateTime", OrderLaws[ZonedDateTime].eqv)
}

