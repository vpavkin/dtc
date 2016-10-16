package dtc.tests

import java.time.{Duration, LocalDate, LocalTime}

import dtc.js.MomentDateTime
import org.scalacheck.Arbitrary._
import org.scalacheck.{Arbitrary, Cogen, Gen}

trait DTCSuiteJS extends DTCSuite {

  val anchorDate = LocalDate.of(1970, 1, 1)

  val daysLimit = 100000000L
  val millisPerDay = 86400000L
  // see http://ecma-international.org/ecma-262/5.1/#sec-15.9.1.1
  implicit val arbLocalDate: Arbitrary[LocalDate] = Arbitrary(
    Gen.choose(-daysLimit, daysLimit).map(anchorDate.plusDays)
  )

  val overflowSafePairGen = for {
    date <- Gen.choose(-daysLimit / 2, daysLimit / 2).map(anchorDate.plusDays)
    time <- arbitrary[LocalTime]
    dur <- Gen.choose(-daysLimit * millisPerDay / 2, daysLimit * millisPerDay / 2).map(Duration.ofMillis)
  } yield (date, time, dur)

  def cogenMomentDateTime[T <: MomentDateTime[T]]: Cogen[T] = Cogen(_.jsGetTime.toLong)
}
