package dtc

import java.time.LocalDate
import syntax.all._

class ControlledTimeProvider[T: Zoned](initialTime: T) extends Provider[T] {

  def currentDate(zone: TimeZoneId): LocalDate = currentTime(zone).date
  def currentTime(zone: TimeZoneId): T = _currentTime.withZoneSameInstant(zone)

  def update(newTime: T): Unit = _currentTime = newTime

  private var _currentTime: T = initialTime
}
