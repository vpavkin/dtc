package dtc.laws

import java.time.LocalDate

import dtc.LawlessDateTimeTC
import org.scalacheck.Prop._
import org.scalacheck.{Arbitrary, Gen}
import dtc.syntax.all._

/**
  * Laws, that must be obeyed by any DateTime typeclass
  */
trait DateTimeLaws[A] {
  implicit def D: LawlessDateTimeTC[A]

  val genA: Gen[A]

  def dateMustNotThrow = forAll(genA) { x: A =>
    D.date(x)
    proved
  }

  def timeMustNotThrow = forAll(genA) { x: A =>
    D.time(x)
    proved
  }

  def dateFieldsAreConsistentWithToLocalDate = forAll(genA) { x: A =>
    (LocalDate.of(x.year, x.month, x.dayOfMonth) ?== x.date) &&
      (x.date.getDayOfWeek ?== x.dayOfWeek)
  }

}

object DateTimeLaws {
  def apply[A](
    implicit
    ev: LawlessDateTimeTC[A],
    arbA: Arbitrary[A]): DateTimeLaws[A] = new DateTimeLaws[A] {
    def D: LawlessDateTimeTC[A] = ev
    val genA = arbA.arbitrary
  }
}
