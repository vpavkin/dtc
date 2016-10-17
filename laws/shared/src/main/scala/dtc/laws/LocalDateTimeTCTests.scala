package dtc.laws

import java.time.{Duration, LocalDate, LocalTime}

import dtc.LocalDateTimeTC
import org.scalacheck.{Arbitrary, Gen}
import org.typelevel.discipline.Laws

trait LocalDateTimeTCTests[A] extends Laws {
  def laws: LocalDateTimeLaws[A]

  def localDateTime(implicit arbA: Arbitrary[A], arbD: Arbitrary[Duration]): RuleSet = {
    new DefaultRuleSet(
      name = "LocalDateTime",
      parent = None,
      "additionAndSubtractionOfSameDuration" -> laws.additionAndSubtractionOfSameDuration,
      "additionOfZero" -> laws.additionOfZero,
      "additionOfNonZero" -> laws.additionOfNonZero,
      "millisAddition" -> laws.millisAddition,
      "secondsAddition" -> laws.secondsAddition,
      "minutesAddition" -> laws.minutesAddition,
      "hoursAddition" -> laws.hoursAddition,
      "twoConsequentNow" -> laws.twoConsequentNowCalls,
      "constructorConsistency" -> laws.constructorConsistency,
      "withYear laws" -> laws.withYear,
      "withMonth laws" -> laws.withMonth,
      "withDayOfMonth laws" -> laws.withDayOfMonth,
      "withHour laws" -> laws.withHour,
      "withMinute laws" -> laws.withMinute,
      "withSecond laws" -> laws.withSecond,
      "withMillisecond laws" -> laws.withMillisecond
    )
  }
}

object LocalDateTimeTCTests {
  def apply[A: LocalDateTimeTC](
    gDateAndDuration: Gen[(A, Duration)],
    gValidYear: Gen[Int])(
    implicit
    arbA: Arbitrary[A],
    arbLocalTime: Arbitrary[LocalTime],
    arbLocalDate: Arbitrary[LocalDate]): LocalDateTimeTCTests[A] = new LocalDateTimeTCTests[A] {
    def laws: LocalDateTimeLaws[A] = LocalDateTimeLaws[A](
      gDateAndDuration, arbLocalTime.arbitrary, arbLocalDate.arbitrary, gValidYear
    )
  }
}
