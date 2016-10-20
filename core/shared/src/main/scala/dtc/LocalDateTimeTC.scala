package dtc

import java.time.{LocalDate, LocalTime}

import simulacrum.typeclass

import scala.language.implicitConversions

@typeclass trait LocalDateTimeTC[A] extends LawlessDateTimeTC[A] {

  def of(date: LocalDate, time: LocalTime): A
  def of(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int, millisecond: Int = 0): A
  def now: A
}
