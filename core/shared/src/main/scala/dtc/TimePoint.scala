package dtc

import java.time.{DayOfWeek, Duration, LocalDate, LocalTime}

import cats.kernel.Order

import scala.language.implicitConversions

/**
  * Common base type class for any dateTime.
  * Doesn't hold any laws, exact behaviour and laws are defined for it's children.
  *
  * All the methods follow java.time._ semantics.
  */
trait TimePoint[A] extends Order[A] {
  /** Date part of x */
  def date(x: A): LocalDate
  /** Time part of x */
  def time(x: A): LocalTime

  /** Add a (possibly negative) duration to this datetime */
  def plus(x: A, d: Duration): A
  /** Subtract a (possibly negative) duration from this datetime */
  def minus(x: A, d: Duration): A
  /** Add a (possibly negative) number of months to this datetime. */
  def plusDays(x: A, days: Int): A
  /** Add a (possibly negative) number of months to this datetime. */
  def plusMonths(x: A, months: Int): A
  /** Add a (possibly negative) number of years to this datetime. */
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
  /** Create a copy of `x` with [[time]] part updated to supplied one. */
  def withTime(x: A, time: LocalTime): A
  /** Create a copy of `x` with [[date]] part updated to supplied one. */
  def withDate(x: A, date: LocalDate): A

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

  /**
    * Get UTC date and time for this instant
    */
  def utc(x: A): (LocalDate, LocalTime)
}

object TimePoint {

  @inline def apply[A](implicit instance: TimePoint[A]): TimePoint[A] = instance

  trait Ops[A] extends Serializable {
    type TypeClassType <: TimePoint[A]
    def self: A
    val typeClassInstance: TypeClassType
    def date: LocalDate = typeClassInstance.date(self)
    def time: LocalTime = typeClassInstance.time(self)
    def plus(d: Duration): A = typeClassInstance.plus(self, d)
    def minus(d: Duration): A = typeClassInstance.minus(self, d)
    def plusDays(days: Int): A = typeClassInstance.plusDays(self, days)
    def plusMonths(months: Int): A = typeClassInstance.plusMonths(self, months)
    def plusYears(years: Int): A = typeClassInstance.plusYears(self, years)
    def withYear(year: Int): A = typeClassInstance.withYear(self, year)
    def withMonth(month: Int): A = typeClassInstance.withMonth(self, month)
    def withDayOfMonth(dayOfMonth: Int): A = typeClassInstance.withDayOfMonth(self, dayOfMonth)
    def withHour(hour: Int): A = typeClassInstance.withHour(self, hour)
    def withMinute(minute: Int): A = typeClassInstance.withMinute(self, minute)
    def withSecond(second: Int): A = typeClassInstance.withSecond(self, second)
    def withMillisecond(millisecond: Int): A = typeClassInstance.withMillisecond(self, millisecond)
    def withTime(time: LocalTime): A = typeClassInstance.withTime(self, time)
    def withDate(date: LocalDate): A = typeClassInstance.withDate(self, date)
    def yearsUntil(until: A): Long = typeClassInstance.yearsUntil(self, until)
    def monthsUntil(until: A): Long = typeClassInstance.monthsUntil(self, until)
    def daysUntil(until: A): Long = typeClassInstance.daysUntil(self, until)
    def hoursUntil(until: A): Long = typeClassInstance.hoursUntil(self, until)
    def minutesUntil(until: A): Long = typeClassInstance.minutesUntil(self, until)
    def secondsUntil(until: A): Long = typeClassInstance.secondsUntil(self, until)
    def millisecondsUntil(until: A): Long = typeClassInstance.millisecondsUntil(self, until)
    def dayOfWeek: DayOfWeek = typeClassInstance.dayOfWeek(self)
    def dayOfMonth: Int = typeClassInstance.dayOfMonth(self)
    def month: Int = typeClassInstance.month(self)
    def year: Int = typeClassInstance.year(self)
    def millisecond: Int = typeClassInstance.millisecond(self)
    def second: Int = typeClassInstance.second(self)
    def minute: Int = typeClassInstance.minute(self)
    def hour: Int = typeClassInstance.hour(self)
    def isEqual(y: A): Boolean = typeClassInstance.isEqual(self, y)
    def isBefore(y: A): Boolean = typeClassInstance.isBefore(self, y)
    def isBeforeOrEquals(y: A): Boolean = typeClassInstance.isBeforeOrEquals(self, y)
    def isAfter(y: A): Boolean = typeClassInstance.isAfter(self, y)
    def isAfterOrEquals(y: A): Boolean = typeClassInstance.isAfterOrEquals(self, y)
    def utc: (LocalDate, LocalTime) = typeClassInstance.utc(self)
  }

  trait ToTimePointOps extends Serializable {
    implicit def toTimePointOps[A](target: A)(implicit tc: TimePoint[A]): Ops[A] { type TypeClassType = TimePoint[A] } =
      new Ops[A] {
        type TypeClassType = TimePoint[A]
        val self: A = target
        val typeClassInstance: TypeClassType = tc
      }
  }

  object nonInheritedOps extends ToTimePointOps

  trait AllOps[A] extends Ops[A] {
    type TypeClassType <: TimePoint[A]
    val typeClassInstance: TypeClassType
  }

  object ops {
    implicit def toAllTimePointOps[A](target: A)(implicit tc: TimePoint[A]): AllOps[A] { type TypeClassType = TimePoint[A] } =
      new AllOps[A] {
        type TypeClassType = TimePoint[A]
        val self: A = target
        val typeClassInstance: TypeClassType = tc
      }
  }
}
