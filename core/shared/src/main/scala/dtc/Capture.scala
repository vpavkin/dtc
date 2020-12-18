package dtc

import java.time.{LocalDate, LocalTime}

import simulacrum.typeclass

/**
  * Provides a way to capture `java.time` based instant into a value of type A.
  */
@typeclass trait Capture[A] {

  /**
    * Java time based constructor for values of type A.
    * Resulting time point will match specified local date and time at specified zone.
    *
    * @param date local date part of A
    * @param time local time part of A
    * @param zone time zone context for specified local date and time
    */
  def capture(date: LocalDate, time: LocalTime, zone: TimeZoneId): A
}

object Capture {

  def apply[A](date: LocalDate, time: LocalTime, zone: TimeZoneId)(implicit C: Capture[A]): A =
    C.capture(date, time, zone)
}
