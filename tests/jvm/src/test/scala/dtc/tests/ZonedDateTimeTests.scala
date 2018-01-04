package dtc.tests

import java.time.temporal.ChronoUnit
import java.time.{Duration, ZonedDateTime}

import cats.instances.option._
import cats.kernel.laws.discipline.OrderTests
import dtc.Offset
import dtc.instances.zonedDateTime._
import dtc.laws.{DateTimeTests, ProviderTests, ZonedDateTimeTestData, ZonedDateTimeTests}
import dtc.syntax.timeZone._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Cogen, Gen}
import dtc.instances.providers.realZonedDateTimeProvider

class JVMZonedDateTimeTests extends DTCSuiteJVM {

  implicit val arbT: Arbitrary[ZonedDateTime] = com.fortysevendeg.scalacheck.datetime.jdk8.ArbitraryJdk8.arbJdk8
  implicit val cogenT: Cogen[ZonedDateTime] = Cogen(_.toEpochSecond)

  val overflowSafePairGen: Gen[(ZonedDateTime, Duration)] = for {
    dt <- arbitrary[ZonedDateTime]
    dur <- arbitrary[Duration]
  } yield (dt, dur)

  def genDateFromPeriod(period: SameZoneOffsetPeriod): Gen[ZonedDateTime] =
    genDateTimeFromSameOffsetPeriod(period).map(tpl => ZonedDateTime.of(tpl._1, tpl._2, tpl._3.zoneId))

  val overflowSafePairGenWithinSameOffset: Gen[(ZonedDateTime, Duration)] = for {
    period <- arbitrary[SameZoneOffsetPeriod]
    dateTime <- genDateFromPeriod(period)
    duration <- genDateFromPeriod(period)
      .map(other => dateTime.until(other, ChronoUnit.NANOS))
      .map(Duration.ofNanos)
  } yield (dateTime, duration)

  val genZonedTestDataSuite: Gen[ZonedDateTimeTestData[ZonedDateTime]] =
    overflowSafePairGen.map {
      case (date, duration) =>
        val target = date.plus(duration)
        ZonedDateTimeTestData(date, duration,
          Offset(date.plus(duration).getOffset.getTotalSeconds), target.toLocalTime, target.toLocalDate)
    }

  checkAll("java.time.ZonedDateTime", DateTimeTests[ZonedDateTime](overflowSafePairGen).dateTime)
  checkAll("java.time.ZonedDateTime", ZonedDateTimeTests[ZonedDateTime](
    overflowSafePairGenWithinSameOffset,
    genZonedTestDataSuite,
    genYear,
    genTimeZone
  ).zonedDateTime)
  checkAll("java.time.ZonedDateTime", OrderTests[ZonedDateTime].order)
  checkAll("java.time.ZonedDateTime", OrderTests[ZonedDateTime].partialOrder)
  checkAll("java.time.ZonedDateTime", OrderTests[ZonedDateTime].eqv)

  checkAll("java.time.ZonedDateTime", ProviderTests[ZonedDateTime](genTimeZone).provider)
}

