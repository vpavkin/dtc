package dtc.instances

import java.time.{Duration, LocalDate, LocalDateTime, LocalTime}

import dtc.LocalDateTimeTC

object localDateTime {
  implicit val localDateTimeDTC: LocalDateTimeTC[LocalDateTime] =
    new LocalDateTimeTC[LocalDateTime] {
      def compare(x: LocalDateTime, y: LocalDateTime): Int = x.compareTo(y)
      def date(x: LocalDateTime): LocalDate = x.toLocalDate
      def time(x: LocalDateTime): LocalTime = x.toLocalTime
      def of(date: LocalDate, time: LocalTime): LocalDateTime = LocalDateTime.of(date, time)
      def plus(x: LocalDateTime, d: Duration): LocalDateTime = x.plus(d)
    }
}
