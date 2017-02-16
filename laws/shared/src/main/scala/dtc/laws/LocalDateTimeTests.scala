package dtc.laws

import java.time.{Duration, LocalDate, LocalTime}

import dtc.Local
import org.scalacheck.{Arbitrary, Gen}
import org.typelevel.discipline.Laws

trait LocalDateTimeTests[A] extends Laws {

  def generalLaws: GeneralLocalDateTimeLaws[A]
  def laws: LocalDateTimeLaws[A]

  def localDateTime(implicit arbA: Arbitrary[A], arbD: Arbitrary[Duration]): RuleSet = {
    new DefaultRuleSet(
      name = "LocalDateTime",
      parent = None,
      "seconds addition laws" -> generalLaws.secondsAddition,
      "minutes addition laws" -> generalLaws.minutesAddition,
      "hours addition laws" -> generalLaws.hoursAddition,
      "two consequent now calls preserve order" -> laws.twoConsequentNowCalls,
      "constructor consistency" -> laws.constructorConsistency,
      "plain constructor consistency" -> laws.plainConstructorConsistency,
      "withYear laws" -> generalLaws.withYear,
      "withMonth laws" -> generalLaws.withMonth,
      "withDayOfMonth laws" -> generalLaws.withDayOfMonth,
      "withHour laws" -> generalLaws.withHour,
      "withMinute laws" -> generalLaws.withMinute,
      "withSecond laws" -> generalLaws.withSecond,
      "withMillisecond laws" -> generalLaws.withMillisecond,
      "daysUntil is consistent with addition" -> generalLaws.daysUntilIsConsistentWithPlus,
      "monthsUntil is consistent with addition" -> generalLaws.monthsUntilIsConsistentWithPlus,
      "yearsUntil counts only number of full years" -> generalLaws.yearsUntilCountsOnlyFullUnits
    )
  }

  // see: https://github.com/moment/moment/issues/3029
  def monthUntilFractionHandling(implicit arbA: Arbitrary[A], arbD: Arbitrary[Duration]): RuleSet = {
    new DefaultRuleSet(
      name = "LocalDateTime",
      parent = None,
      "monthsUntil counts only number of full months" -> generalLaws.monthsUntilCountsOnlyFullUnits
    )
  }
}

object LocalDateTimeTests {
  def apply[A: Local](
    gDateAndDuration: Gen[(A, Duration)],
    gValidYear: Gen[Int])(
    implicit
    arbA: Arbitrary[A],
    arbLocalTime: Arbitrary[LocalTime],
    arbLocalDate: Arbitrary[LocalDate]): LocalDateTimeTests[A] = new LocalDateTimeTests[A] {

    def laws: LocalDateTimeLaws[A] = LocalDateTimeLaws[A](
      arbLocalTime.arbitrary, arbLocalDate.arbitrary
    )

    def generalLaws: GeneralLocalDateTimeLaws[A] = GeneralLocalDateTimeLaws[A](
      gDateAndDuration, arbLocalTime.arbitrary, arbLocalDate.arbitrary, gValidYear
    )
  }
}
