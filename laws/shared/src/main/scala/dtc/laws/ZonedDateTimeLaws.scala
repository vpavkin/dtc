package dtc.laws


import java.time.temporal.ChronoUnit
import java.time.{Duration, LocalDate, LocalTime}

import cats.kernel.laws._
import dtc._
import dtc.syntax.zonedDateTime._
import org.scalacheck.Prop._
import org.scalacheck.{Arbitrary, Gen}

/**
  * Laws, that must be obeyed by any ZonedDateTimeTC instance
  */
trait ZonedDateTimeLaws[A] {
  implicit def D: ZonedDateTimeTC[A]

  val genA: Gen[A]
  val genDateAndDurationWithinSameOffset: Gen[(A, Duration)]
  val genLocalDate: Gen[LocalDate]
  val genLocalTime: Gen[LocalTime]
  val genValidYear: Gen[Int]
  val genTimeZone: Gen[TimeZoneId]

  def twoConsequentNowCalls = forAll(genTimeZone) { zone1: TimeZoneId =>
    val prev = D.now(zone1)
    val current = D.now(zone1)
    prev ?<= current
  }

  def constructorConsistency = forAll(genLocalDate, genLocalTime, genTimeZone) {
    (date: LocalDate, time: LocalTime, zone: TimeZoneId) =>
      val dt = D.of(date, time, zone)
      (dt.date ?== date) &&
        (dt.time ?== time.truncatedTo(ChronoUnit.MILLIS)) &&
        (dt.zone ?== zone)
  }
}

object ZonedDateTimeLaws {
  def apply[A](
    gDateAndDurationWithinSameDST: Gen[(A, Duration)],
    gLocalTime: Gen[LocalTime],
    gLocalDate: Gen[LocalDate],
    gValidYear: Gen[Int],
    gTimeZone: Gen[TimeZoneId])(
    implicit ev: ZonedDateTimeTC[A],
    arbA: Arbitrary[A]): ZonedDateTimeLaws[A] = new ZonedDateTimeLaws[A] {

    def D: ZonedDateTimeTC[A] = ev

    val genTimeZone: Gen[TimeZoneId] = gTimeZone
    val genDateAndDurationWithinSameOffset: Gen[(A, Duration)] = gDateAndDurationWithinSameDST
    val genLocalDate: Gen[LocalDate] = gLocalDate
    val genLocalTime: Gen[LocalTime] = gLocalTime
    val genValidYear: Gen[Int] = gValidYear
    val genA: Gen[A] = arbA.arbitrary
  }
}
