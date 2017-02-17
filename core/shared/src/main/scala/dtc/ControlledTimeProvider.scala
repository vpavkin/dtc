package dtc

import java.time.LocalDate

/**
  * An artificial time provider that is controlled from the outside.
  *
  * @param initialTime initial time to start with
  */
class ControlledTimeProvider[T](initialTime: T)(implicit Z: Zoned[T]) extends Provider[T] {

  def currentDate(zone: TimeZoneId): LocalDate = Z.date(currentTime(zone))
  def currentTime(zone: TimeZoneId): T = Z.withZoneSameInstant(_currentTime, zone)

  /**
    * Current value in provider
    */
  def value: T = _currentTime

  /**
    * Set new "current" time
    */
  def update(newTime: T): Unit = _currentTime = newTime

  private var _currentTime: T = initialTime
}
