package ru.pavkin.dtc.instances

import java.time.{LocalDate, LocalDateTime, LocalTime}

import ru.pavkin.dtc.LocalDateTimeTC

object localDateTime {
  implicit val localDateTimeDTC: LocalDateTimeTC[LocalDateTime] =
    new LocalDateTimeTC[LocalDateTime] {
      def compare(x: LocalDateTime, y: LocalDateTime): Int = x.compareTo(y)
      def date(x: LocalDateTime): LocalDate = x.toLocalDate
      def time(x: LocalDateTime): LocalTime = x.toLocalTime
      def of(date: LocalDate, time: LocalTime): LocalDateTime = LocalDateTime.of(date, time)
    }
}
