package dtc.instances

import java.time.{LocalDate, LocalTime}

import dtc.{TimeZoneId, ZonedDateTimeTC}
import dtc.js.MomentDateTime


object moment {
  implicit val momentZonedDTC: ZonedDateTimeTC[MomentDateTime] =
    new ZonedDateTimeTC[MomentDateTime] {

      def of(date: LocalDate, time: LocalTime, zone: TimeZoneId): MomentDateTime =
        MomentDateTime.of(date, time, zone)

      def withZoneSameInstant(x: MomentDateTime, zone: TimeZoneId): MomentDateTime =
        x.withZoneSameInstant(zone)

      def withZoneSameLocal(x: MomentDateTime, zone: TimeZoneId): MomentDateTime =
        x.withZoneSameLocal(zone)

      def zone(x: MomentDateTime): TimeZoneId =
        x.zone

      def date(x: MomentDateTime): LocalDate = x.toLocalDate
      def time(x: MomentDateTime): LocalTime = x.toLocalTime

      def compare(x: MomentDateTime, y: MomentDateTime): Int = MomentDateTime.compare(x, y)
    }
}
