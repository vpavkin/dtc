package dtc

import java.time.{LocalDate, LocalTime}

import simulacrum.typeclass

import scala.language.implicitConversions

@typeclass trait ZonedDateTimeTC[A] extends LawlessDateTimeTC[A] {

  def of(date: LocalDate, time: LocalTime, zone: TimeZoneId): A
  def withZoneSameInstant(x: A, zone: TimeZoneId): A
  def withZoneSameLocal(x: A, zone: TimeZoneId): A
  def zone(x: A): TimeZoneId
  def now(zone: TimeZoneId): A
}
