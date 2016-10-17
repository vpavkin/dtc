package dtc.laws

import java.time.temporal.ChronoUnit
import java.time.{Duration, LocalDate, LocalTime}

import cats.kernel.instances.int._
import dtc.LocalDateTimeTC
import dtc.syntax.localDateTime._
import org.scalacheck.Gen
import org.scalacheck.Prop._

/**
  * Laws, that must be obeyed by any LocalDateTimeTC instance
  */
trait LocalDateTimeLaws[A] {
  implicit def D: LocalDateTimeTC[A]

  val genAdditionSafeDateAndDuration: Gen[(A, Duration)]
  val genLocalDate: Gen[LocalDate]
  val genLocalTime: Gen[LocalTime]

  def additionAndSubtractionOfSameDuration = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    D.plus(D.plus(x, d), d.negated()) ?== x
  }

  def additionOfZero = forAll(genAdditionSafeDateAndDuration) { case (x, _) =>
    D.plus(x, Duration.ZERO) ?== x
  }

  def additionOfNonZero = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    d.isZero ?|| D.neqv(D.plus(x, d), x)
  }

  def millisAddition = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    D.plus(x, d).millisecond ?== ((x.millisecond + d.toMillis) %% 1000)
  }

  def secondsAddition = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    val seconds = d.toMillis / 1000
    val updated = D.plus(x, Duration.ofSeconds(seconds))
    (updated.second ?== ((x.second + seconds) %% 60)) &&
      notChanged(x, updated)("ms", _.millisecond)
  }

  def minutesAddition = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    val updated = D.plus(x, Duration.ofMinutes(d.toMinutes))
    val validator = notChanged(x, updated)
    (updated.minute ?== ((x.minute + d.toMinutes) %% 60)) &&
      validator("s, ms", _.millisecond, _.second)
  }

  def hoursAddition = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    val updated = D.plus(x, Duration.ofHours(d.toHours))
    val validator = notChanged(x, updated)
    (updated.hour ?== ((x.hour + d.toHours) %% 24)) &&
      validator("m, s, ms", _.minute, _.second, _.millisecond)
  }

  def twoConsequentNowCalls = {
    val prev = D.now
    val current = D.now
    prev ?<= current
  }

  def constructorConsistency = forAll(genLocalDate, genLocalTime) { (date: LocalDate, time: LocalTime) =>
    val dt = D.of(date, time)
    (dt.date ?== date) && (dt.time ?== time.truncatedTo(ChronoUnit.MILLIS))
  }
}

object LocalDateTimeLaws {
  def apply[A](
    gDateAndDuration: Gen[(A, Duration)],
    gLocalTime: Gen[LocalTime],
    gLocalDate: Gen[LocalDate])(
    implicit ev: LocalDateTimeTC[A]): LocalDateTimeLaws[A] = new LocalDateTimeLaws[A] {
    def D: LocalDateTimeTC[A] = ev
    val genAdditionSafeDateAndDuration: Gen[(A, Duration)] = gDateAndDuration
    val genLocalDate: Gen[LocalDate] = gLocalDate
    val genLocalTime: Gen[LocalTime] = gLocalTime
  }
}
