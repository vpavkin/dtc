package dtc.laws


import cats.Order
import cats.kernel.laws._
import dtc.{Provider, TimeZoneId}
import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Gen, Prop}

trait ProviderLaws[A] {

  implicit def P: Provider[A]
  implicit def O: Order[A]

  val genA: Gen[A]
  val genTimeZone: Gen[TimeZoneId]

  def twoConsequentNowCalls: Prop = forAll(genTimeZone) { zone: TimeZoneId =>
    val prev = P.currentTime(zone)
    val current = P.currentTime(zone)
    prev ?<= current
  }
}

object ProviderLaws {
  def apply[A](
    gTimeZone: Gen[TimeZoneId])(
    implicit order: Order[A],
    provider: Provider[A],
    arbA: Arbitrary[A]): ProviderLaws[A] = new ProviderLaws[A] {

    implicit def P: Provider[A] = provider
    implicit def O: Order[A] = order

    val genTimeZone: Gen[TimeZoneId] = gTimeZone
    val genA: Gen[A] = arbA.arbitrary
  }
}
