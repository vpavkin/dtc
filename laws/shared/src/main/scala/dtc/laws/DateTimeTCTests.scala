package dtc.laws

import dtc.LawlessDateTimeTC
import org.scalacheck.Arbitrary
import org.typelevel.discipline.Laws

trait DateTimeTCTests[A] extends Laws {
  def laws: DateTimeLaws[A]

  def dateTime(implicit arbA: Arbitrary[A]): RuleSet = {
    new DefaultRuleSet(
      name = "DateTime",
      parent = None,
      "date is always defined" -> laws.dateMustNotThrow,
      "time is always defined" -> laws.timeMustNotThrow
    )
  }
}

object DateTimeTCTests {
  def apply[A: LawlessDateTimeTC](
    implicit arbA: Arbitrary[A]): DateTimeTCTests[A] = new DateTimeTCTests[A] {
    def laws: DateTimeLaws[A] = DateTimeLaws[A]
  }
}
