package dtc.tests

import java.time.temporal.ChronoField
import java.time.{LocalDate, LocalTime}

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FunSuite, Matchers}
import org.typelevel.discipline.scalatest.Discipline

abstract class DTCSuite extends FunSuite
  with Matchers
  with GeneratorDrivenPropertyChecks
  with Discipline {

  private val epochDayRange = ChronoField.EPOCH_DAY.range()
  private val nanoOfDayRange = ChronoField.NANO_OF_DAY.range()

  implicit val arbLocalDate: Arbitrary[LocalDate] = Arbitrary(
    Gen.choose(epochDayRange.getMinimum, epochDayRange.getMaximum).map(LocalDate.ofEpochDay)
  )

  implicit val arbLocalTime: Arbitrary[LocalTime] = Arbitrary(
    Gen.choose(nanoOfDayRange.getMinimum, nanoOfDayRange.getMaximum).map(LocalTime.ofNanoOfDay)
  )
}
