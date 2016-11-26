package dtc.laws

import java.time.{Duration, LocalDate, LocalTime}

import dtc.{TimeZoneId, ZonedDateTimeTC}
import org.scalacheck.{Arbitrary, Gen}
import org.typelevel.discipline.Laws

trait ZonedDateTimeTCTests[A] extends Laws {

  def generalLocalDateTimeLaws: GeneralLocalDateTimeLaws[A]
  def laws: ZonedDateTimeLaws[A]

  def zonedDateTime(implicit arbA: Arbitrary[A], arbD: Arbitrary[Duration]): RuleSet = {
    new DefaultRuleSet(
      name = "ZonedDateTime",
      parent = None,
      "[within same offset] seconds addition laws" -> generalLocalDateTimeLaws.secondsAddition,
      "[within same offset] minutes addition laws" -> generalLocalDateTimeLaws.minutesAddition,
      "[within same offset] hours addition laws" -> generalLocalDateTimeLaws.hoursAddition,
      "[within same offset] withYear laws" -> generalLocalDateTimeLaws.withYear,
      "[within same offset] withMonth laws" -> generalLocalDateTimeLaws.withMonth,
      "[within same offset] withDayOfMonth laws" -> generalLocalDateTimeLaws.withDayOfMonth,
      "[within same offset] withHour laws" -> generalLocalDateTimeLaws.withHour,
      "[within same offset] withMinute laws" -> generalLocalDateTimeLaws.withMinute,
      "[within same offset] withSecond laws" -> generalLocalDateTimeLaws.withSecond,
      "[within same offset] withMillisecond laws" -> generalLocalDateTimeLaws.withMillisecond,
      "[within same offset] daysUntil is consistent with addition" -> generalLocalDateTimeLaws.daysUntilIsConsistentWithPlus,
      "[within same offset] monthsUntil is consistent with addition" -> generalLocalDateTimeLaws.monthsUntilIsConsistentWithPlus,
      "[within same offset] yearsUntil counts only number of full years" -> generalLocalDateTimeLaws.yearsUntilCountsOnlyFullUnits,
      "two consequent now calls preserve order" -> laws.twoConsequentNowCalls,
      "constructor consistency" -> laws.constructorConsistency,
      "cross-offset addition" -> laws.crossOffsetAddition
    )
  }
}

object ZonedDateTimeTCTests {
  def apply[A: ZonedDateTimeTC](
    gDateAndDurationWithinSameDST: Gen[(A, Duration)],
    gDataSuite: Gen[ZonedDateTimeTestData[A]],
    gValidYear: Gen[Int],
    gTimeZone: Gen[TimeZoneId])(
    implicit
    arbA: Arbitrary[A],
    arbLocalTime: Arbitrary[LocalTime],
    arbLocalDate: Arbitrary[LocalDate]): ZonedDateTimeTCTests[A] = new ZonedDateTimeTCTests[A] {

    def generalLocalDateTimeLaws: GeneralLocalDateTimeLaws[A] = GeneralLocalDateTimeLaws[A](
      gDateAndDurationWithinSameDST, arbLocalTime.arbitrary, arbLocalDate.arbitrary, gValidYear
    )

    def laws: ZonedDateTimeLaws[A] = ZonedDateTimeLaws[A](
      gDateAndDurationWithinSameDST, gDataSuite,
      arbLocalTime.arbitrary, arbLocalDate.arbitrary, gValidYear, gTimeZone
    )
  }
}
