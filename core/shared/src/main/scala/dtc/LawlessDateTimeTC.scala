package dtc

import java.time.{DayOfWeek, Duration, LocalDate, LocalTime}

import cats.kernel.Order
import simulacrum.typeclass

import scala.language.implicitConversions

@typeclass(excludeParents = List("Order"))
trait LawlessDateTimeTC[A] extends Order[A] {
  def date(x: A): LocalDate
  def time(x: A): LocalTime

  def plus(x: A, d: Duration): A
  def plusMonths(x: A, months: Int): A
  def plusYears(x: A, years: Int): A

  def withYear(x: A, year: Int): A
  def withMonth(x: A, month: Int): A
  def withDayOfMonth(x: A, dayOfMonth: Int): A
  def withHour(x: A, hour: Int): A
  def withMinute(x: A, minute: Int): A
  def withSecond(x: A, second: Int): A
  def withMillisecond(x: A, millisecond: Int): A

  def yearsUntil(x: A, until: A): Long
  def monthsUntil(x: A, until: A): Long
  def daysUntil(x: A, until: A): Long
  def hoursUntil(x: A, until: A): Long
  def minutesUntil(x: A, until: A): Long
  def secondsUntil(x: A, until: A): Long
  def millisecondsUntil(x: A, until: A): Long

  def dayOfWeek(x: A): DayOfWeek
  def dayOfMonth(x: A): Int
  def month(x: A): Int
  def year(x: A): Int
  def millisecond(x: A): Int
  def second(x: A): Int
  def minute(x: A): Int
  def hour(x: A): Int
}
