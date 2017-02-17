package dtc.laws

import java.time.Duration

import cats.Order
import dtc.{Provider, TimeZoneId}
import org.scalacheck.{Arbitrary, Gen}
import org.typelevel.discipline.Laws

trait ProviderTests[A] extends Laws {

  def laws: ProviderLaws[A]

  def provider(implicit arbA: Arbitrary[A], arbD: Arbitrary[Duration]): RuleSet = {
    new DefaultRuleSet(
      name = "Provider",
      parent = None,
      "two consequent now calls preserve order" -> laws.twoConsequentNowCalls
    )
  }
}

object ProviderTests {
  def apply[A: Provider : Order](
    gTimeZone: Gen[TimeZoneId])(
    implicit arbA: Arbitrary[A]): ProviderTests[A] = new ProviderTests[A] {
    def laws: ProviderLaws[A] = ProviderLaws(gTimeZone)
  }
}
