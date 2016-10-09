package dtc.syntax

import java.time.ZoneId

import dtc.TimeZoneId

object timeZone {

  implicit final class TimeZoneIdOps(val timeZone: TimeZoneId) {
    final def zoneId: ZoneId = ZoneId.of(timeZone.id)
  }

  implicit final class JVMZoneIdOps(val zoneId: ZoneId) extends AnyVal {
    final def dtcZoneId: TimeZoneId = TimeZoneId(zoneId.getId)
  }
}
