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
      "seconds addition laws" -> laws.secondsAddition,
      "minutes addition laws" -> laws.minutesAddition,
      "hours addition laws" -> laws.hoursAddition,
      "two consequent now calls preserve order" -> laws.twoConsequentNowCalls,
      "constructor consistency" -> laws.constructorConsistency,
      "plain constructor consistency" -> laws.plainConstructorConsistency,
      "withYear laws" -> laws.withYear,
      "withMonth laws" -> laws.withMonth,
      "withDayOfMonth laws" -> laws.withDayOfMonth,
      "withHour laws" -> laws.withHour,
      "withMinute laws" -> laws.withMinute,
      "withSecond laws" -> laws.withSecond,
      "withMillisecond laws" -> laws.withMillisecond,
      "daysUntil is consistent with addition" -> laws.daysUntilIsConsistentWithPlus,
      "monthsUntil is consistent with addition" -> laws.monthsUntilIsConsistentWithPlus,
      "yearsUntil counts only number of full years" -> laws.yearsUntilCountsOnlyFullUnits
    )
  }

  // see: https://github.com/moment/moment/issues/3029
  def monthUntilFractionHandling(implicit arbA: Arbitrary[A], arbD: Arbitrary[Duration]): RuleSet = {
    new DefaultRuleSet(
      name = "LocalDateTime",
      parent = None,
      "monthsUntil counts only number of full months" -> laws.monthsUntilCountsOnlyFullUnits
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
