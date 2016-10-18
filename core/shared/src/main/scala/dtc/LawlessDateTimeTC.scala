package dtc

import java.time.{Duration, LocalDate, LocalTime}

import cats.kernel.Order
import simulacrum.typeclass

import scala.language.implicitConversions

@typeclass(excludeParents = List("Order"))
trait LawlessDateTimeTC[A] extends Order[A] {
  def date(x: A): LocalDate
  def time(x: A): LocalTime
  def plus(x: A, d: Duration): A
  def withYear(x: A, year: Int): A
  def withMonth(x: A, month: Int): A
  def withDayOfMonth(x: A, dayOfMonth: Int): A
  def withHour(x: A, hour: Int): A
  def withMinute(x: A, minute: Int): A
  def withSecond(x: A, second: Int): A
  def withMillisecond(x: A, millisecond: Int): A
  def hoursUntil(x: A, until: A): Long
  def minutesUntil(x: A, until: A): Long
  def secondsUntil(x: A, until: A): Long
  def millisecondsUntil(x: A, until: A): Long
}
