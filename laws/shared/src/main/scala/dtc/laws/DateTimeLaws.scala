package dtc.laws

import java.time.{Duration, LocalDate, LocalTime}

import dtc._
import cats.kernel.instances.int._
import cats.kernel.instances.long._
import dtc.LawlessDateTimeTC
import org.scalacheck.Prop.{proved => _, _}
import org.scalacheck.{Arbitrary, Gen}
import dtc.syntax.all._
import cats.kernel.laws._

/**
  * Laws, that must be obeyed by any DateTime typeclass
  */
trait DateTimeLaws[A] {
  implicit def D: LawlessDateTimeTC[A]

  val genA: Gen[A]
  val genAdditionSafeDateAndDuration: Gen[(A, Duration)]

  // take into account that nanos are always positive in the Duration.
  private def fullNumberOfSeconds(d: Duration) = {
    val seconds = d.getSeconds
    if (seconds >= 0 || d.getNano == 0) seconds
    else seconds + 1
  }

  def additionAndSubtractionOfSameDuration = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    D.plus(D.plus(x, d), d.negated()) ?== x
  }

  def additionOfZero = forAll(genAdditionSafeDateAndDuration) { case (x, _) =>
    D.plus(x, Duration.ZERO) ?== x
  }

  def additionOfNonZero = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    d.isZero ?||
      ((d.isNegative && D.lt(D.plus(x, d), x)) || D.gt(D.plus(x, d), x))
  }

  def millisAddition = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    D.plus(x, d).millisecond ?== ((x.millisecond + d.toMillis) %% 1000)
  }

  def untilSelfIsAlwaysZero = forAll(genA) { x: A =>
    (D.millisecondsUntil(x, x) ?== 0L) &&
      (D.secondsUntil(x, x) ?== 0L) &&
      (D.minutesUntil(x, x) ?== 0L) &&
      (D.hoursUntil(x, x) ?== 0L) &&
      (D.daysUntil(x, x) ?== 0L) &&
      (D.monthsUntil(x, x) ?== 0L) &&
      (D.yearsUntil(x, x) ?== 0L)
  }

  def untilIsConsistentWithPlus = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    val altered = D.plus(x, d)
    val truncated = truncateToMillis(d)
    (D.millisecondsUntil(x, altered) ?== truncated.toMillis) &&
      (D.secondsUntil(x, altered) ?== fullNumberOfSeconds(truncated)) &&
      (D.minutesUntil(x, altered) ?== fullNumberOfSeconds(truncated) / SecondsInMinute) &&
      (D.hoursUntil(x, altered) ?== fullNumberOfSeconds(truncated) / (SecondsInMinute * MinutesInHour))
  }

  def dateMustNotThrow = forAll(genA) { x: A =>
    D.date(x)
    proved
  }

  def timeMustNotThrow = forAll(genA) { x: A =>
    D.time(x)
    proved
  }

  def dateFieldsAreConsistentWithToLocalDate = forAll(genA) { x: A =>
    (LocalDate.of(x.year, x.month, x.dayOfMonth) ?== x.date) &&
      (x.date.getDayOfWeek ?== x.dayOfWeek)
  }

  def timeFieldsAreConsistentWithToLocalTime = forAll(genA) { x: A =>
    LocalTime.of(x.hour, x.minute, x.second, millisToNanos(x.millisecond)) ?== x.time
  }

}

object DateTimeLaws {
  def apply[A](gDateAndDuration: Gen[(A, Duration)])(
    implicit
    ev: LawlessDateTimeTC[A],
    arbA: Arbitrary[A]): DateTimeLaws[A] = new DateTimeLaws[A] {
    def D: LawlessDateTimeTC[A] = ev
    val genA = arbA.arbitrary
    val genAdditionSafeDateAndDuration = gDateAndDuration
  }
}
