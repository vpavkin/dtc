package ru.pavkin.dtc.instances

import java.time.{LocalDate, LocalTime}

import ru.pavkin.dtc.LocalDateTimeTC
import ru.pavkin.dtc.js.JSDate

object jsDate {
  implicit val jsDateLocalDTC: LocalDateTimeTC[JSDate] =
    new LocalDateTimeTC[JSDate] {
      def of(date: LocalDate, time: LocalTime): JSDate = JSDate.of(date, time)
      def date(x: JSDate): LocalDate = x.toLocalDate
      def time(x: JSDate): LocalTime = x.toLocalTime
    }
}
