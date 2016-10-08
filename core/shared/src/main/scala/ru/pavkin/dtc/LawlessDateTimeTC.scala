package ru.pavkin.dtc

import java.time.{LocalDate, LocalTime}

import simulacrum._

import scala.language.implicitConversions

@typeclass trait LawlessDateTimeTC[A] {
  def date(x: A): LocalDate
  def time(x: A): LocalTime
  def dayOfMonth(x: A): Int = date(x).getDayOfMonth
  def month(x: A): Int = date(x).getMonthValue
  def year(x: A): Int = date(x).getYear
}
