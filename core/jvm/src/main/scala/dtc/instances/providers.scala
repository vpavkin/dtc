package dtc.instances

import java.time.{LocalDate, LocalDateTime, ZonedDateTime}

import dtc.{Provider, TimeZoneId, syntax}
import syntax.timeZone._

object providers {

  implicit val realLocalDateTimeProvider: Provider[LocalDateTime] = new Provider[LocalDateTime] {
    def currentTime(zone: TimeZoneId): LocalDateTime = LocalDateTime.now(zone.zoneId)
    def currentDate(zone: TimeZoneId): LocalDate = LocalDate.now(zone.zoneId)
  }

  implicit val realZonedDateTimeProvider: Provider[ZonedDateTime] = new Provider[ZonedDateTime] {
    def currentTime(zone: TimeZoneId): ZonedDateTime = ZonedDateTime.now(zone.zoneId)
    def currentDate(zone: TimeZoneId): LocalDate = LocalDate.now(zone.zoneId)
  }

}
