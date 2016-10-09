package dtc

import java.time.{LocalDate, LocalTime}

import simulacrum.typeclass

import scala.language.implicitConversions

@typeclass trait LocalDateTimeTC[A] extends LawlessDateTimeTC[A] {

  def of(date: LocalDate, time: LocalTime): A
}
