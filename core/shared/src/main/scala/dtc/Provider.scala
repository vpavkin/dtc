package dtc

import java.time.LocalDate

import simulacrum.typeclass

import scala.language.implicitConversions

/**
  * An abstraction that allows the concept of "current time" to be injected and controlled
  */
@typeclass trait Provider[T] {

  def currentDate(zone: TimeZoneId): LocalDate
  def currentTime(zone: TimeZoneId): T
}
