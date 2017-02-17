package dtc.laws

import java.time.Duration

import dtc.Lawless
import org.scalacheck.{Arbitrary, Gen}
import org.typelevel.discipline.Laws

trait DateTimeTests[A] extends Laws {
  def laws: DateTimeLaws[A]

  def dateTime(implicit arbA: Arbitrary[A]): RuleSet = {
    new DefaultRuleSet(
      name = "DateTime",
      parent = None,
      "add and substract the same duration gives original value" -> laws.additionAndSubtractionOfSameDuration,
      "add zero gives same value" -> laws.additionOfZero,
      "add non zero changes value" -> laws.additionOfNonZero,
      "millis addition laws" -> laws.millisAddition,
      "until self is always zero" -> laws.untilSelfIsAlwaysZero,
      "until methods are consistent with addition" -> laws.untilIsConsistentWithPlus,
      "date is always defined" -> laws.dateMustNotThrow,
      "time is always defined" -> laws.timeMustNotThrow,
      "date fields are consistent with toLocalDate" -> laws.dateFieldsAreConsistentWithToLocalDate,
      "time fields are consistent with toLocalTime" -> laws.timeFieldsAreConsistentWithToLocalTime
    )
  }
}

object DateTimeTests {
  def apply[A: Lawless](
    gDateAndDuration: Gen[(A, Duration)])(
    implicit arbA: Arbitrary[A]): DateTimeTests[A] = new DateTimeTests[A] {
    def laws: DateTimeLaws[A] = DateTimeLaws[A](gDateAndDuration)
  }
}
