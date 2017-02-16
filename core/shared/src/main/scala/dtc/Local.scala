package dtc

import java.time.{LocalDate, LocalTime}

import simulacrum.typeclass

import scala.language.implicitConversions

/**
  * A typeclass for local datetime values, that are unaware of DST and offsets.
  *
  * Semantics are similar to `java.time.LocalDateTime`
  */
@typeclass trait Local[A] extends Lawless[A] {

  /**
    * Java time based constructor for values of type A
    *
    * @param date date part of A
    * @param time time part of A
    */
  def of(date: LocalDate, time: LocalTime): A

  /**
    * Plain constructor for values of type A
    *
    * @param year        year value
    * @param month       month, 1-12
    * @param day         day, must be a valid day for supplied month and year
    * @param hour        hour, 0-23
    * @param minute      minute, 0-60
    * @param second      second, 0-60
    * @param millisecond millisecond, 0-999
    */
  def of(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int, millisecond: Int = 0): A

  /** Current system time */
  def now: A
}
