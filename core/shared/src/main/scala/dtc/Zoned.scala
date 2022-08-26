package dtc

import simulacrum.typeclass

/**
  * A typeclass for zoned datetime values. These values are zone aware and follow DST transformation rules.
  *
  * Semantics are similar to `java.time.ZonedDateTime`
  */
@typeclass trait Zoned[A] extends TimePoint[A] with Capture[A] {

  /**
    * Returns a new date time value with timezone altered in a way,
    * that preserves exact moment in time, described by `x`
    *
    * @param x    original date-time
    * @param zone time zone to create new value in
    */
  def withZoneSameInstant(x: A, zone: TimeZoneId): A

  /**
    * Returns a new date time value with timezone altered.
    * Local date and time stay the same.
    *
    * This means that if supplied zone has different offset, new value will have different instant.
    *
    * @param x    original date-time
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
}


