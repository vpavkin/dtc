package dtc.tests

import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit._
import java.time.{Duration, LocalDate, LocalTime}

import dtc.TimeZoneId
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

  val genLocalTime: Gen[LocalTime] =
    Gen.choose(nanoOfDayRange.getMinimum, nanoOfDayRange.getMaximum).map(LocalTime.ofNanoOfDay)
  implicit val arbLocalTime: Arbitrary[LocalTime] = Arbitrary(genLocalTime)

  val genDuration: Gen[Duration] =
    Gen.choose(Long.MinValue / 1000, Long.MaxValue / 1000)
      .map(l => Duration.of(l, MILLIS))

  implicit val arbDuration = Arbitrary(genDuration)

  // todo [2]
  def genDateTimeFromSameOffsetPeriod(period: SameZoneOffsetPeriod): Gen[(LocalDate, LocalTime, TimeZoneId)] = for {
    date <- Gen.choose(period.startDate.toEpochDay + 1L, period.endDate.toEpochDay - 1L).map(LocalDate.ofEpochDay)
    timeBounds <- Gen.const(
      if (date == period.startDate && date == period.endDate) (period.startTime, period.endTime)
      else if (date == period.startDate) (period.startTime, LocalTime.MAX)
      else if (date == period.endDate) (LocalTime.MAX, period.endTime)
      else (LocalTime.MIN, LocalTime.MAX)
    )
    time <- Gen.choose(timeBounds._1.toNanoOfDay, timeBounds._2.toNanoOfDay).map(LocalTime.ofNanoOfDay)
  } yield (date, time, period.zone)
}
