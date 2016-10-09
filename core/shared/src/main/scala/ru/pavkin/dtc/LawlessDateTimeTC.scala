package ru.pavkin.dtc

import java.time.{DayOfWeek, LocalDate, LocalTime}
import cats.kernel.Order
import simulacrum._

import scala.language.implicitConversions

@typeclass(excludeParents = List("Order"))
trait LawlessDateTimeTC[A] extends Order[A] {
  def date(x: A): LocalDate
  def time(x: A): LocalTime
  def dayOfWeek(x: A): DayOfWeek = date(x).getDayOfWeek
  def dayOfMonth(x: A): Int = date(x).getDayOfMonth
  def month(x: A): Int = date(x).getMonthValue
  def year(x: A): Int = date(x).getYear
}
