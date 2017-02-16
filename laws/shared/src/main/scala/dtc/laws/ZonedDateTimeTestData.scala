package dtc.laws

import java.time.{Duration, LocalDate, LocalTime}

import dtc.{Offset, Zoned}

case class ZonedDateTimeTestData[A: Zoned](
  source: A,
  diff: Duration,
  targetOffset: Offset,
  targetTime: LocalTime,
  targetDate: LocalDate)
