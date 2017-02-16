package dtc

import java.time.{DayOfWeek, Duration, LocalDate, LocalTime}

import cats.kernel.Order
import simulacrum.typeclass

import scala.language.implicitConversions

/**
  * Common base type class for any dateTime.
  * Doesn't hold any laws, exact behaviour and laws are defined for it's children.
  *
  * All the methods follow java.time._ semantics.
  */
@typeclass(excludeParents = List("Order"))
trait Lawless[A] extends Order[A] {
  /** date part of x */
  def date(x: A): LocalDate
  /** time part of x */
  def time(x: A): LocalTime

  /** add (possibly negative) duration to this datetime */
  def plus(x: A, d: Duration): A
  /** Add (possibly negative) number of months to this datetime. */
  def plusMonths(x: A, months: Int): A
  /** Add (possibly negative) number of years to this datetime. */
  def plusYears(x: A, years: Int): A

  /** Create a copy of `x` with year altered. */
  def withYear(x: A, year: Int): A
  /** Create a copy of `x` with month altered. Month value has to be in [1,12] range */
  def withMonth(x: A, month: Int): A
  /** Create a copy of `x` with day of month altered. */
  def withDayOfMonth(x: A, dayOfMonth: Int): A
  /** Create a copy of `x` with hour of day altered. */
  def withHour(x: A, hour: Int): A
  /** Create a copy of `x` with minute if hour altered. */
  def withMinute(x: A, minute: Int): A
  /** Create a copy of `x` with second of minute altered. */
  def withSecond(x: A, second: Int): A
  /** Create a copy of `x` with millisecond of second altered. */
  def withMillisecond(x: A, millisecond: Int): A

  /** Returns number of full years between two datetime values. Result can be negative if `until` < `x` */
  def yearsUntil(x: A, until: A): Long
  /** Returns number of full months between two datetime values. Result can be negative if `until` < `x` */
  def monthsUntil(x: A, until: A): Long
  /** Returns number of full days between two datetime values. Result can be negative if `until` < `x` */
  def daysUntil(x: A, until: A): Long
  /** Returns number of full hours between two datetime values. Result can be negative if `until` < `x` */
  def hoursUntil(x: A, until: A): Long
  /** Returns number of full minutes between two datetime values. Result can be negative if `until` < `x` */
  def minutesUntil(x: A, until: A): Long
  /** Returns number of full seconds between two datetime values. Result can be negative if `until` < `x` */
  def secondsUntil(x: A, until: A): Long
  /** Returns number of milliseconds between two datetime values. Result can be negative if `until` < `x` */
  def millisecondsUntil(x: A, until: A): Long

  def dayOfWeek(x: A): DayOfWeek
  def dayOfMonth(x: A): Int
  def month(x: A): Int
  def year(x: A): Int
  def millisecond(x: A): Int
  def second(x: A): Int
  def minute(x: A): Int
  def hour(x: A): Int

  // these are comparison helpers so that there's a convenient comparison syntax without cats.syntax in place
  def isEqual(x: A, y: A): Boolean = eqv(x, y)
  def isBefore(x: A, y: A): Boolean = lt(x, y)
  def isBeforeOrEquals(x: A, y: A): Boolean = lteqv(x, y)
  def isAfter(x: A, y: A): Boolean = gt(x, y)
  def isAfterOrEquals(x: A, y: A): Boolean = gteqv(x, y)
}
