package dtc.laws

import java.time.Duration

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
      "hoursAddition" -> laws.hoursAddition
    )
  }
}

object LocalDateTimeTCTests {
  def apply[A: LocalDateTimeTC](
    gen: Gen[(A, Duration)]): LocalDateTimeTCTests[A] = new LocalDateTimeTCTests[A] {
    def laws: LocalDateTimeLaws[A] = LocalDateTimeLaws[A](gen)
  }
}
