package dtc

import java.time.{LocalDate, LocalTime}

import scala.language.implicitConversions

/**
  * Provides a way to capture `java.time` based instant into a value of type A.
  */
trait Capture[A] extends Serializable {

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

object Capture extends Serializable {

  @inline def apply[A](implicit instance: Capture[A]): Capture[A] = instance

  trait Ops[A] extends Serializable {
    type TypeClassType <: Capture[A]
    def self: A
    val typeClassInstance: TypeClassType
  }

  trait ToCaptureOps extends Serializable {
    implicit def toCaptureOps[A](target: A)(implicit tc: Capture[A]): Ops[A] { type TypeClassType = Capture[A] } =
      new Ops[A] {
        type TypeClassType = Capture[A]
        val self: A = target
        val typeClassInstance: TypeClassType = tc
      }
  }

  object nonInheritedOps extends ToCaptureOps

  trait AllOps[A] extends Ops[A] {
    type TypeClassType <: Capture[A]
    val typeClassInstance: TypeClassType
  }

  object ops {
    implicit def toAllCaptureOps[A](target: A)(implicit tc: Capture[A]): AllOps[A] { type TypeClassType = Capture[A] } =
      new AllOps[A] {
        type TypeClassType = Capture[A]
        val self: A = target
        val typeClassInstance: TypeClassType = tc
      }
  }

  def apply[A](date: LocalDate, time: LocalTime, zone: TimeZoneId)(implicit C: Capture[A]): A =
    C.capture(date, time, zone)
}
