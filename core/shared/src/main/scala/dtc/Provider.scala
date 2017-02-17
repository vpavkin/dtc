package dtc

import java.time.LocalDate

import simulacrum.typeclass

import scala.language.implicitConversions

/**
  * An abstraction that allows the concept of "current time" to be injected and controlled
  */
@typeclass trait Provider[T] {

  /**
    * Current date at specified zone
    */
  def currentDate(zone: TimeZoneId): LocalDate

  /**
    * Current time at specified zone
    */
  def currentTime(zone: TimeZoneId): T
}
