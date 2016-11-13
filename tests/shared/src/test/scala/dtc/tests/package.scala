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
    SameZoneOffsetPeriod(LocalDate.of(2016, 3, 27), LocalTime.of(4, 0), LocalDate.of(2016, 10, 30), LocalTime.of(3, 0), TimeZoneId("Europe/Riga")),
    SameZoneOffsetPeriod(LocalDate.of(2011, 3, 27), LocalTime.of(4, 0), LocalDate.of(2014, 10, 26), LocalTime.of(0, 0), TimeZoneId("Asia/Yakutsk")),
    SameZoneOffsetPeriod(LocalDate.of(2014, 10, 26), LocalTime.of(5, 0), LocalDate.of(2019, 1, 1), LocalTime.of(0, 0), TimeZoneId("Asia/Yakutsk")),
    SameZoneOffsetPeriod(LocalDate.of(1951, 1, 1), LocalTime.of(0, 0), LocalDate.of(1996, 4, 7), LocalTime.of(0, 0), TimeZoneId("America/Mexico_City"))
  )

  val genSameOffsetPeriod = Gen.oneOf(sameOffsetZonedTimePeriods)

  implicit val arbSameOffsetPeriod = Arbitrary(genSameOffsetPeriod)

}
