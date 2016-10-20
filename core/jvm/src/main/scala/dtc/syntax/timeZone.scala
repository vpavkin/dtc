package dtc.syntax

import java.time.ZoneId

import dtc.TimeZoneId

/**
  * Contains syntax helpers for easier conversions to and from [[java.time.ZoneId]]
  */
object timeZone {

  implicit final class TimeZoneIdOps(val timeZone: TimeZoneId) {
    final def zoneId: ZoneId = ZoneId.of(timeZone.id)
  }

  implicit final class JVMZoneIdOps(val zoneId: ZoneId) extends AnyVal {
    final def dtcZoneId: TimeZoneId = TimeZoneId(zoneId.getId)
  }
}
