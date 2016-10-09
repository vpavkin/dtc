package ru.pavkin.dtc

import java.time.{LocalDate, LocalTime}

import cats.kernel.Order
import simulacrum._

import scala.language.implicitConversions

@typeclass(excludeParents = List("Order"))
trait LawlessDateTimeTC[A] extends Order[A] {
  def date(x: A): LocalDate
  def time(x: A): LocalTime
}
