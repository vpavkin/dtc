package dtc.instances

import java.time.{LocalDate, LocalTime, ZonedDateTime}

import dtc.{TimeZoneId, ZonedDateTimeTC}
import dtc.syntax.timeZone._

object zonedDateTime {
  implicit val zonedDateTimeDTC: ZonedDateTimeTC[ZonedDateTime] =
    new ZonedDateTimeTC[ZonedDateTime] {
      def of(date: LocalDate, time: LocalTime, zone: TimeZoneId): ZonedDateTime =
        ZonedDateTime.of(date, time, zone.zoneId)
      def withZoneSameInstant(x: ZonedDateTime, zone: TimeZoneId): ZonedDateTime =
        x.withZoneSameInstant(zone.zoneId)
      def withZoneSameLocal(x: ZonedDateTime, zone: TimeZoneId): ZonedDateTime =
        x.withZoneSameLocal(zone.zoneId)
      def zone(x: ZonedDateTime): TimeZoneId =
        TimeZoneId(x.getZone.getId)
      def date(x: ZonedDateTime): LocalDate = x.toLocalDate
      def time(x: ZonedDateTime): LocalTime = x.toLocalTime
      def compare(x: ZonedDateTime, y: ZonedDateTime): Int = x.compareTo(y)
    }
}
