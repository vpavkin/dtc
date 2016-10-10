package dtc.laws

import dtc.LawlessDateTimeTC
import org.scalacheck.Arbitrary
import org.scalacheck.Prop._
import org.typelevel.discipline.Laws

trait DateTimeTCTests[A] extends Laws {
  def laws: DateTimeLaws[A]

  def dateTime(implicit arbA: Arbitrary[A]): RuleSet = {
    new DefaultRuleSet(
      name = "DateTime",
      parent = None,
      "date is always defined" -> forAll(laws.dateMustNotThrow _),
      "time is always defined" -> forAll(laws.timeMustNotThrow _))
  }
}

object DateTimeTCTests {
  def apply[A: LawlessDateTimeTC]: DateTimeTCTests[A] = new DateTimeTCTests[A] {
    def laws: DateTimeLaws[A] = DateTimeLaws[A]
  }
}
