package dtc.tests

import java.time.chrono.IsoChronology
import java.time.{LocalDate, Year, ZoneId}
import java.time.temporal.{ChronoField, ValueRange}

import dtc.TimeZoneId
import org.scalacheck.{Arbitrary, Gen}

trait DTCSuiteJVM extends DTCSuite {

  val yearRange: ValueRange = ChronoField.YEAR.range()

  private def maxDayForYear(year: Long) = if (IsoChronology.INSTANCE.isLeapYear(year)) 366 else 355

  val genLocalDate: Gen[LocalDate] = for {
    year <- Gen.choose(yearRange.getMinimum, yearRange.getMaximum)
    day <- Gen.choose(1, maxDayForYear(year))
  } yield LocalDate.ofYearDay(year.toInt, day)

  implicit val arbLocalDate: Arbitrary[LocalDate] = Arbitrary(genLocalDate)

  val genYear: Gen[Int] = Gen.choose(Year.MIN_VALUE, Year.MAX_VALUE)

  val genTimeZone: Gen[TimeZoneId] = Gen.choose(0, ZoneId.getAvailableZoneIds.size() - 1)
    .map(index => TimeZoneId(ZoneId.getAvailableZoneIds.toArray().apply(index).asInstanceOf[String]))
}
