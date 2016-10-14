package dtc.instances

import java.time.{Duration, LocalDate, LocalTime}

import dtc.{LawlessDateTimeTC, TimeZoneId, ZonedDateTimeTC}
import dtc.js._


object moment {
  implicit val momentZonedDTC: ZonedDateTimeTC[MomentZonedDateTime] =
    new ZonedDateTimeTC[MomentZonedDateTime] {

      def of(date: LocalDate, time: LocalTime, zone: TimeZoneId): MomentZonedDateTime =
        MomentZonedDateTime.of(date, time, zone)

      def withZoneSameInstant(x: MomentZonedDateTime, zone: TimeZoneId): MomentZonedDateTime =
        x.withZoneSameInstant(zone)

      def withZoneSameLocal(x: MomentZonedDateTime, zone: TimeZoneId): MomentZonedDateTime =
        x.withZoneSameLocal(zone)

      def zone(x: MomentZonedDateTime): TimeZoneId =
        x.zone

      def date(x: MomentZonedDateTime): LocalDate = x.toLocalDate
      def time(x: MomentZonedDateTime): LocalTime = x.toLocalTime

      def compare(x: MomentZonedDateTime, y: MomentZonedDateTime): Int = MomentDateTime.compare(x, y)
      def plus(x: MomentZonedDateTime, d: Duration): MomentZonedDateTime = x.plus(d)
    }

  implicit val momentLocalDTC: LawlessDateTimeTC[MomentLocalDateTime] =
    new LawlessDateTimeTC[MomentLocalDateTime] {
      def date(x: MomentLocalDateTime): LocalDate = x.toLocalDate
      def time(x: MomentLocalDateTime): LocalTime = x.toLocalTime
      def plus(x: MomentLocalDateTime, d: Duration): MomentLocalDateTime = x.plus(d)
      def compare(x: MomentLocalDateTime, y: MomentLocalDateTime): Int = MomentDateTime.compare(x, y)
    }
}
