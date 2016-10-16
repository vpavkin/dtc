package dtc.tests

import java.time.LocalDate
import java.time.temporal.ChronoField

import org.scalacheck.{Arbitrary, Gen}

trait DTCSuiteJVM extends DTCSuite {

  private val epochDayRange = ChronoField.EPOCH_DAY.range()

  implicit val arbLocalDate: Arbitrary[LocalDate] = Arbitrary(
    Gen.choose(epochDayRange.getMinimum, epochDayRange.getMaximum).map(LocalDate.ofEpochDay)
  )
}
