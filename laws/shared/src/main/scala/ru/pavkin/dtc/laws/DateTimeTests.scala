package ru.pavkin.dtc.laws

import org.scalacheck.Arbitrary
import org.scalacheck.Prop._
import org.typelevel.discipline.Laws
import ru.pavkin.dtc.DateTime

trait DateTimeTests[A] extends Laws {
  def laws: DateTimeLaws[A]

  def dateTime(implicit arbA: Arbitrary[A]): RuleSet = {
    new DefaultRuleSet(
      name = "DateTime",
      parent = None,
      "DateTime date is always defined" -> forAll(laws.dateMustNotThrow _),
      "DateTime time is always defined" -> forAll(laws.timeMustNotThrow _))
  }
}

object DateTimeTests {
  def apply[A: DateTime]: DateTimeTests[A] = new DateTimeTests[A] {
    def laws: DateTimeLaws[A] = DateTimeLaws[A]
  }
}
