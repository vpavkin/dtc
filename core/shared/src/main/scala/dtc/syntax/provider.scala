package dtc.syntax

import java.time.{LocalDate, YearMonth}

import dtc.{Provider, TimeZoneId}

object provider extends Provider.ToProviderOps {
  @inline def currentTime[T](zone: TimeZoneId)(implicit P: Provider[T]): T = P.currentTime(zone)

  @inline def now[T](zone: TimeZoneId)(implicit P: Provider[T]): T = currentTime(zone)

  @inline def currentDate(zone: TimeZoneId)(implicit P: Provider[_]): LocalDate = P.currentDate(zone)

  @inline def currentMonth(zone: TimeZoneId)(implicit P: Provider[_]): YearMonth =
    YearMonth.from(P.currentDate(zone))
}
