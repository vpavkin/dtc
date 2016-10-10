package dtc.tests

import java.time.LocalDate

import dtc.LawlessDateTimeTC
import org.scalacheck.Arbitrary
import dtc.syntax.localDateTime._

abstract class ExtendedSyntaxTests[T: LawlessDateTimeTC] extends DTCSuite {

  implicit def arbT: Arbitrary[T]

  test("date fields are consistent with date part") {
    forAll { x: T =>
      LocalDate.of(x.year, x.month, x.dayOfMonth) shouldBe x.date
      x.date.getDayOfWeek shouldBe x.dayOfWeek
    }
  }


}
