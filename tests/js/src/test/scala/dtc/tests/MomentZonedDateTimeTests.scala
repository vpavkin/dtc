package dtc.tests

import java.time.{Duration, LocalDate, LocalTime}

import cats.kernel.laws.OrderLaws
import dtc.{TimeZoneId, Zoned}
import dtc.js.MomentZonedDateTime
import dtc.laws.{DateTimeTests, ProviderTests, ZonedDateTimeTestData, ZonedDateTimeTests}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Cogen, Gen}
import dtc.instances.moment.providers.realMomentZonedDateTimeProvider

abstract class MomentZonedDateTimeTests(instance: Zoned[MomentZonedDateTime]) extends DTCSuiteJS {

  implicit val zonedInstance: Zoned[MomentZonedDateTime] = instance

  implicit val arbT: Arbitrary[MomentZonedDateTime] = Arbitrary(for {
    date <- arbitrary[LocalDate]
    time <- arbitrary[LocalTime]
    zone <- arbitrary[TimeZoneId]
  } yield MomentZonedDateTime.of(date, time, zone))

  implicit val cogenT: Cogen[MomentZonedDateTime] = cogenMomentDateTime[MomentZonedDateTime]

  val pairGen: Gen[(MomentZonedDateTime, Duration)] = for {
    zone <- arbitrary[TimeZoneId]
    pair <- overflowSafePairGen
  } yield (MomentZonedDateTime.of(pair._1, pair._2, zone), pair._3)

  def genDateFromPeriod(period: SameZoneOffsetPeriod): Gen[MomentZonedDateTime] =
    genDateTimeFromSameOffsetPeriod(period).map(tpl => MomentZonedDateTime.of(tpl._1, tpl._2, tpl._3))

  val overflowSafePairGenWithinSameOffset: Gen[(MomentZonedDateTime, Duration)] = for {
    period <- arbitrary[SameZoneOffsetPeriod]
    dateTime <- genDateFromPeriod(period)
    duration <- genDateFromPeriod(period)
      .map(other => dateTime.millisecondsUntil(other))
      .map(Duration.ofMillis)
  } yield (dateTime, duration)


  val genZonedTestDataSuite: Gen[ZonedDateTimeTestData[MomentZonedDateTime]] =
    pairGen.map {
      case (date, duration) =>
        val target = date.plus(duration)
        ZonedDateTimeTestData(date, duration, target.offset, target.toLocalTime, target.toLocalDate)
    }

  checkAll("MomentZonedDateTime", DateTimeTests[MomentZonedDateTime](pairGen).dateTime)
  checkAll("MomentZonedDateTime", ZonedDateTimeTests[MomentZonedDateTime](
    overflowSafePairGenWithinSameOffset,
    genZonedTestDataSuite,
    genJSValidYear,
    genTimeZone
  ).zonedDateTime)
  checkAll("MomentZonedDateTime", OrderLaws[MomentZonedDateTime].order)
  checkAll("MomentZonedDateTime", OrderLaws[MomentZonedDateTime].partialOrder)
  checkAll("MomentZonedDateTime", OrderLaws[MomentZonedDateTime].eqv)

  checkAll("MomentZonedDateTime", ProviderTests[MomentZonedDateTime](genTimeZone).provider)
}

class MomentZonedDateTimeWithStrictEqualityTests
  extends MomentZonedDateTimeTests(dtc.instances.moment.momentZonedWithStrictEquality)

class MomentZonedDateTimeWithCrossZoneEqualityTests
  extends MomentZonedDateTimeTests(dtc.instances.moment.momentZonedWithCrossZoneEquality)

