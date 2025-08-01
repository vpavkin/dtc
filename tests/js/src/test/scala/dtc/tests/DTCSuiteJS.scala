package dtc.tests

import java.time.{Duration, LocalDate, LocalTime}

import dtc.TimeZoneId
import dtc.tests.platform._
import dtc.js.MomentDateTime
import org.scalacheck.Arbitrary._
import org.scalacheck.{Arbitrary, Cogen, Gen}

trait DTCSuiteJS extends DTCSuite {

  val anchorDate: LocalDate = LocalDate.of(1970, 1, 1)

  val daysLimit: Long = 100000000L
  val millisPerDay: Long = 86400000L

  // see http://ecma-international.org/ecma-262/5.1/#sec-15.9.1.1
  val genLocalDate: Gen[LocalDate] = Gen.choose(-daysLimit, daysLimit).map(anchorDate.plusDays)
  implicit val arbLocalDate: Arbitrary[LocalDate] = Arbitrary(genLocalDate)

  val genJSValidYear: Gen[Int] = genLocalDate.map(_.getYear).map(y => if (y < 0) y + 1 else y - 1)

  val genTimeZone: Gen[TimeZoneId] = Gen.oneOf(availableZoneIds).map(TimeZoneId(_))

  implicit val arbTimeZone: Arbitrary[TimeZoneId] = Arbitrary(genTimeZone)

  val overflowSafePairGen: Gen[(LocalDate, LocalTime, Duration)] = for {
    date <- Gen.choose(-daysLimit / 2, daysLimit / 2).map(anchorDate.plusDays)
    time <- arbitrary[LocalTime]
    dur <- Gen.choose(-daysLimit * millisPerDay / 2, daysLimit * millisPerDay / 2).map(Duration.ofMillis)
  } yield (date, time, dur)

  def cogenMomentDateTime[T <: MomentDateTime[T]]: Cogen[T] = Cogen(_.jsGetTime.toLong)
}
