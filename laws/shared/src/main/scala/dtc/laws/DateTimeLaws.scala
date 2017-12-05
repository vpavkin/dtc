package dtc.laws

import java.time.{Duration, LocalDate, LocalTime}

import dtc._
import cats.kernel.instances.int._
import cats.kernel.instances.long._
import dtc.TimePoint
import org.scalacheck.Prop.{proved => _, _}
import org.scalacheck.{Arbitrary, Gen, Prop}
import dtc.syntax.all._
import cats.kernel.laws._

/**
  * Laws, that must be obeyed by any DateTime typeclass
  */
trait DateTimeLaws[A] {
  implicit def D: TimePoint[A]

  val genA: Gen[A]
  val genAdditionSafeDateAndDuration: Gen[(A, Duration)]

  // take into account that nanos are always positive in the Duration.
  private def fullNumberOfSeconds(d: Duration) = {
    val seconds = d.getSeconds
    if (seconds >= 0 || d.getNano == 0) seconds
    else seconds + 1
  }

  def additionAndSubtractionOfSameDuration: Prop = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    D.plus(D.plus(x, d), d.negated()) ?== x
  }

  def additionOfZero: Prop = forAll(genAdditionSafeDateAndDuration) { case (x, _) =>
    D.plus(x, Duration.ZERO) ?== x
  }

  def additionOfNonZero: Prop = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    d.isZero ?||
      ((d.isNegative && D.lt(D.plus(x, d), x)) || D.gt(D.plus(x, d), x))
  }

  def millisAddition: Prop = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    D.plus(x, d).millisecond ?== ((x.millisecond + d.toMillis) %% 1000)
  }

  def untilSelfIsAlwaysZero: Prop = forAll(genA) { x: A =>
    (D.millisecondsUntil(x, x) ?== 0L) &&
      (D.secondsUntil(x, x) ?== 0L) &&
      (D.minutesUntil(x, x) ?== 0L) &&
      (D.hoursUntil(x, x) ?== 0L) &&
      (D.daysUntil(x, x) ?== 0L) &&
      (D.monthsUntil(x, x) ?== 0L) &&
      (D.yearsUntil(x, x) ?== 0L)
  }

  def untilIsConsistentWithPlus: Prop = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    val altered = D.plus(x, d)
    val truncated = truncateToMillis(d)
    (D.millisecondsUntil(x, altered) ?== truncated.toMillis) &&
      (D.secondsUntil(x, altered) ?== fullNumberOfSeconds(truncated)) &&
      (D.minutesUntil(x, altered) ?== fullNumberOfSeconds(truncated) / SecondsInMinute) &&
      (D.hoursUntil(x, altered) ?== fullNumberOfSeconds(truncated) / (SecondsInMinute * MinutesInHour))
  }

  def dateMustNotThrow: Prop = forAll(genA) { x: A =>
    D.date(x)
    proved
  }

  def timeMustNotThrow: Prop = forAll(genA) { x: A =>
    D.time(x)
    proved
  }

  def dateFieldsAreConsistentWithToLocalDate: Prop = forAll(genA) { x: A =>
    (LocalDate.of(x.year, x.month, x.dayOfMonth) ?== x.date) &&
      (x.date.getDayOfWeek ?== x.dayOfWeek)
  }

  def timeFieldsAreConsistentWithToLocalTime: Prop = forAll(genA) { x: A =>
    LocalTime.of(x.hour, x.minute, x.second, millisToNanos(x.millisecond)) ?== x.time
  }

}

object DateTimeLaws {
  def apply[A](gDateAndDuration: Gen[(A, Duration)])(
    implicit
    ev: TimePoint[A],
    arbA: Arbitrary[A]): DateTimeLaws[A] = new DateTimeLaws[A] {
    def D: TimePoint[A] = ev
    val genA: Gen[A] = arbA.arbitrary
    val genAdditionSafeDateAndDuration: Gen[(A, Duration)] = gDateAndDuration
  }
}
