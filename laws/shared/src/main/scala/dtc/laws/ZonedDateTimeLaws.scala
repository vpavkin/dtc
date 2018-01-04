package dtc.laws


import java.time.temporal.ChronoUnit
import java.time.{Duration, LocalDate, LocalTime}

import cats.kernel.laws.discipline.{catsLawsIsEqToProp => p}
import cats.kernel.laws._
import cats.instances.long._
import dtc._
import dtc.syntax.zoned._
import org.scalacheck.Prop._
import org.scalacheck.{Arbitrary, Gen, Prop}

/**
  * Laws, that must be obeyed by any Zoned instance
  */
trait ZonedDateTimeLaws[A] {
  implicit def D: Zoned[A]

  val genA: Gen[A]
  val genDateAndDurationWithinSameOffset: Gen[(A, Duration)]
  val genDataSuite: Gen[ZonedDateTimeTestData[A]]
  val genLocalDate: Gen[LocalDate]
  val genLocalTime: Gen[LocalTime]
  val genValidYear: Gen[Int]
  val genTimeZone: Gen[TimeZoneId]

  def crossOffsetAddition: Prop = forAll(genDataSuite) { data =>
    val target = D.plus(data.source, data.diff)
    p(D.offset(target) <-> data.targetOffset) &&
      (D.date(target) <-> data.targetDate) &&
      (D.time(target) <-> data.targetTime.truncatedTo(ChronoUnit.MILLIS))
  }

  def localTimeAndOffsetCorrelation: Prop = forAll(genA, genTimeZone) { (date: A, zone: TimeZoneId) =>
    val target = D.withZoneSameInstant(date, zone)
    D.time(date) <-> D.time(target).plusSeconds((date.offset.seconds - target.offset.seconds).toLong)
  }

  def withZoneSameInstantGivesSameInstant: Prop = forAll(genA, genTimeZone) { (date: A, zone: TimeZoneId) =>
    val target = D.withZoneSameInstant(date, zone)
    p(D.zone(target) <-> zone) &&
      (D.millisecondsUntil(date, target) <-> 0L)
  }
}

object ZonedDateTimeLaws {
  def apply[A](
    gDateAndDurationWithinSameDST: Gen[(A, Duration)],
    gDataSuite: Gen[ZonedDateTimeTestData[A]],
    gLocalTime: Gen[LocalTime],
    gLocalDate: Gen[LocalDate],
    gValidYear: Gen[Int],
    gTimeZone: Gen[TimeZoneId])(
    implicit ev: Zoned[A],
    arbA: Arbitrary[A]): ZonedDateTimeLaws[A] = new ZonedDateTimeLaws[A] {

    def D: Zoned[A] = ev

    val genTimeZone: Gen[TimeZoneId] = gTimeZone
    val genDateAndDurationWithinSameOffset: Gen[(A, Duration)] = gDateAndDurationWithinSameDST
    val genDataSuite: Gen[ZonedDateTimeTestData[A]] = gDataSuite
    val genLocalDate: Gen[LocalDate] = gLocalDate
    val genLocalTime: Gen[LocalTime] = gLocalTime
    val genValidYear: Gen[Int] = gValidYear
    val genA: Gen[A] = arbA.arbitrary
  }
}
