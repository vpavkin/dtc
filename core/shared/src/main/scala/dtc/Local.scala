package dtc

import java.time.{LocalDate, LocalTime}

import scala.language.implicitConversions

/**
  * A typeclass for local datetime values, that are unaware of DST and offsets.
  * Can be also viewed as UTC time.
  *
  * Semantics are similar to `java.time.LocalDateTime`
  */
trait Local[A] extends TimePoint[A] {

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

  override def utc(x: A): (LocalDate, LocalTime) = date(x) -> time(x)
}

object Local {

  @inline def apply[A](implicit instance: Local[A]): Local[A] = instance

  trait Ops[A] extends Serializable {
    type TypeClassType <: Local[A]
    def self: A
    val typeClassInstance: TypeClassType
  }

  trait ToLocalOps extends Serializable {
    implicit def toLocalOps[A](target: A)(implicit tc: Local[A]): Ops[A] { type TypeClassType = Local[A] } =
      new Ops[A] {
        type TypeClassType = Local[A]
        val self: A = target
        val typeClassInstance: TypeClassType = tc
      }
  }

  object nonInheritedOps extends ToLocalOps

  trait AllOps[A] extends Ops[A] with TimePoint.AllOps[A] {
    type TypeClassType <: Local[A]
    val typeClassInstance: TypeClassType
  }

  object ops {
    implicit def toAllLocalOps[A](target: A)(implicit tc: Local[A]): AllOps[A] { type TypeClassType = Local[A] } =
      new AllOps[A] {
        type TypeClassType = Local[A]
        val self: A = target
        val typeClassInstance: TypeClassType = tc
      }
  }

  /**
    * Create a new local A value from provided date and time.
    */
  def of[A](date: LocalDate, time: LocalTime)(implicit Z: Local[A]): A = Z.of(date, time)
}
