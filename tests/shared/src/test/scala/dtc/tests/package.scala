package dtc

import java.time.{LocalDate, LocalTime}

import org.scalacheck.{Arbitrary, Gen}

package object tests {

  case class SameZoneOffsetPeriod(
    startDate: LocalDate, startTime: LocalTime,
    endDate: LocalDate, endTime: LocalTime,
    zone: TimeZoneId)

  // scalastyle:off
  val sameOffsetZonedTimePeriods: List[SameZoneOffsetPeriod] = List(
    SameZoneOffsetPeriod(LocalDate.of(2016, 3, 27), LocalTime.of(4, 0), LocalDate.of(2016, 10, 30), LocalTime.of(3, 0), TimeZoneId("Europe/Riga"))
  )

  val genSameOffsetPeriod = Gen.oneOf(sameOffsetZonedTimePeriods)

  implicit val arbSameOffsetPeriod = Arbitrary(genSameOffsetPeriod)

}
