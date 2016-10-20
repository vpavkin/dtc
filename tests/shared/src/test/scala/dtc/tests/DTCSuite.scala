package dtc.tests

import java.time.{Duration, LocalTime}
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit._

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FunSuiteLike, Matchers}
import org.typelevel.discipline.scalatest.Discipline

trait DTCSuite extends FunSuiteLike
  with Matchers
  with GeneratorDrivenPropertyChecks
  with Discipline {


  override implicit val generatorDrivenConfig: PropertyCheckConfiguration = PropertyCheckConfiguration(
    minSuccessful = 100
  )
  private val nanoOfDayRange = ChronoField.NANO_OF_DAY.range()

  val genLocalTime = Gen.choose(nanoOfDayRange.getMinimum, nanoOfDayRange.getMaximum).map(LocalTime.ofNanoOfDay)
  implicit val arbLocalTime: Arbitrary[LocalTime] = Arbitrary(genLocalTime)

  val genDuration: Gen[Duration] =
    Gen.choose(Long.MinValue / 1000, Long.MaxValue / 1000)
      .map(l => Duration.of(l, MILLIS))

  implicit val arbDuration = Arbitrary(genDuration)
}
