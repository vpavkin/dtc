package dtc.laws

import java.time.{Duration, LocalDate, LocalTime}

import dtc.{Offset, ZonedDateTimeTC}

case class ZonedDateTimeTestData[A: ZonedDateTimeTC](
  source: A,
  diff: Duration,
  targetOffset: Offset,
  targetTime: LocalTime,
  targetDate: LocalDate)
