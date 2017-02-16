package dtc.instances.moment

import java.time.LocalDate

import dtc.js.{MomentLocalDateTime, MomentZonedDateTime}
import dtc.{Provider, TimeZoneId}

object providers {
  implicit val realMomentLocalDateTimeProvider: Provider[MomentLocalDateTime] = new Provider[MomentLocalDateTime] {
    def currentDate(zone: TimeZoneId): LocalDate = currentTime(zone).toLocalDate
    def currentTime(zone: TimeZoneId): MomentLocalDateTime = MomentLocalDateTime.now(zone)
  }

  implicit val realMomentZonedDateTimeProvider: Provider[MomentZonedDateTime] = new Provider[MomentZonedDateTime] {
    def currentDate(zone: TimeZoneId): LocalDate = currentTime(zone).toLocalDate
    def currentTime(zone: TimeZoneId): MomentZonedDateTime = MomentZonedDateTime.now(zone)
  }
}
