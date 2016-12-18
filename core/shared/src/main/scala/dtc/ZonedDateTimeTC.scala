package dtc

import java.time.{LocalDate, LocalTime}

import simulacrum.typeclass

import scala.language.implicitConversions

/**
  * A typeclass for zoned datetime values. These values are zone aware and follow DST transformation rules.
  *
  * Semantics are similar to `java.time.ZonedDateTime`
  */
@typeclass trait ZonedDateTimeTC[A] extends LawlessDateTimeTC[A] {

  /**
    * Java time based constructor for values of type A
    *
    * @param date local date part of A
    * @param time local time part of A
    * @param zone time zone for produced value
    */
  def of(date: LocalDate, time: LocalTime, zone: TimeZoneId): A

  /**
    * Returns a new zoned date time value with timezone altered in a way,
    * that preserves exact moment time, described by `x`
    *
    * @param x    original zoned datetime
    * @param zone time zone to create new value in
    */
  def withZoneSameInstant(x: A, zone: TimeZoneId): A

  /**
    * Returns a new zoned date time value with timezone altered.
    * Local date and time stay the same.
    *
    * This means that if supplied zone has different offset, new value will have different instant.
    *
    * @param x    original zoned datetime
    * @param zone time zone to create new value in
    */
  def withZoneSameLocal(x: A, zone: TimeZoneId): A

  /**
    * Get the timezone of x
    */
  def zone(x: A): TimeZoneId

  /**
    * UTC offset of this datetime value
    */
  def offset(x: A): Offset

  /**
    * Get current system time for supplied timezone
    */
  def now(zone: TimeZoneId): A
}
