package dtc.tests

import java.time.{Duration, LocalDate, LocalTime}

import cats.kernel.laws.OrderLaws
import dtc.TimeZoneId
import dtc.instances.moment._
import dtc.js.MomentZonedDateTime
import dtc.laws.{DateTimeTCTests, ZonedDateTimeTCTests}
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary

class MomentZonedDateTimeTests extends DTCSuiteJS {

  implicit val arbT: Arbitrary[MomentZonedDateTime] = Arbitrary(for {
    date <- arbitrary[LocalDate]
    time <- arbitrary[LocalTime]
    zone <- arbitrary[TimeZoneId]
  } yield MomentZonedDateTime.of(date, time, zone))

  implicit val cogenT = cogenMomentDateTime[MomentZonedDateTime]

  val pairGen = for {
    zone <- arbitrary[TimeZoneId]
    pair <- overflowSafePairGen
  } yield (MomentZonedDateTime.of(pair._1, pair._2, zone), pair._3)

  def genDateFromPeriod(period: SameZoneOffsetPeriod) =
    genDateTimeFromSameOffsetPeriod(period).map(tpl => MomentZonedDateTime.of(tpl._1, tpl._2, tpl._3))

  val overflowSafePairGenWithinSameOffset = for {
    period <- arbitrary[SameZoneOffsetPeriod]
    dateTime <- genDateFromPeriod(period)
    duration <- genDateFromPeriod(period)
      .map(other => dateTime.millisecondsUntil(other))
      .map(Duration.ofMillis)
  } yield (dateTime, duration)

  checkAll("MomentZonedDateTime", DateTimeTCTests[MomentZonedDateTime](pairGen).dateTime)
  checkAll("MomentZonedDateTime", ZonedDateTimeTCTests[MomentZonedDateTime](
    overflowSafePairGenWithinSameOffset,
    genJSValidYear,
    genTimeZone
  ).zonedDateTime)
  checkAll("MomentZonedDateTime", OrderLaws[MomentZonedDateTime].order)
  checkAll("MomentZonedDateTime", OrderLaws[MomentZonedDateTime].partialOrder)
  checkAll("MomentZonedDateTime", OrderLaws[MomentZonedDateTime].eqv)
}

