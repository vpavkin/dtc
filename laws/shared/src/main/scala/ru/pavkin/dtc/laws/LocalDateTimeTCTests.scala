package ru.pavkin.dtc.laws

import org.scalacheck.Arbitrary
import org.scalacheck.Prop._
import org.typelevel.discipline.Laws
import ru.pavkin.dtc.LocalDateTimeTC

trait LocalDateTimeTCTests[A] extends Laws {
  def laws: LocalDateTimeLaws[A]

  def localDateTime(implicit arbA: Arbitrary[A]): RuleSet = {
    new DefaultRuleSet(
      name = "LocalDateTimeTC",
      parent = None,
      "date is always defined" -> forAll(laws.dateMustNotThrow _),
      "time is always defined" -> forAll(laws.timeMustNotThrow _))
  }
}

object LocalDateTimeTCTests {
  def apply[A: LocalDateTimeTC]: LocalDateTimeTCTests[A] = new LocalDateTimeTCTests[A] {
    def laws: LocalDateTimeLaws[A] = LocalDateTimeLaws[A]
  }
}
