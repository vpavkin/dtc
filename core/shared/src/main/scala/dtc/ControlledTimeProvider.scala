package dtc

import java.time.LocalDate

class ControlledTimeProvider[T](initialTime: T)(implicit Z: Zoned[T]) extends Provider[T] {

  def currentDate(zone: TimeZoneId): LocalDate = Z.date(currentTime(zone))
  def currentTime(zone: TimeZoneId): T = Z.withZoneSameInstant(_currentTime, zone)

  def value: T = _currentTime
  def update(newTime: T): Unit = _currentTime = newTime

  private var _currentTime: T = initialTime
}
