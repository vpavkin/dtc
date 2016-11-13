package dtc.tests

import java.time.{LocalDate, Year, ZoneId}
import java.time.temporal.ChronoField

import dtc.TimeZoneId
import org.scalacheck.{Arbitrary, Gen}

import scala.collection.JavaConverters._

trait DTCSuiteJVM extends DTCSuite {

  private val epochDayRange = ChronoField.EPOCH_DAY.range()

  val genLocalDate = Gen.choose(epochDayRange.getMinimum, epochDayRange.getMaximum).map(LocalDate.ofEpochDay)
  implicit val arbLocalDate: Arbitrary[LocalDate] = Arbitrary(genLocalDate)

  val genYear = Gen.choose(Year.MIN_VALUE, Year.MAX_VALUE)

  val genTimeZone = Gen.oneOf(ZoneId.getAvailableZoneIds.asScala.toSeq).map(TimeZoneId(_))
}
