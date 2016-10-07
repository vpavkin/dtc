package ru.pavkin.dtc.instances

import java.time.{LocalDate, LocalDateTime, LocalTime}

import ru.pavkin.dtc.DateTime

object localDateTime {
  implicit val localDateTimeDTC: DateTime[LocalDateTime] =
    new DateTime[LocalDateTime] {
      def date(x: LocalDateTime): LocalDate = x.toLocalDate
      def time(x: LocalDateTime): LocalTime = x.toLocalTime
    }
}
