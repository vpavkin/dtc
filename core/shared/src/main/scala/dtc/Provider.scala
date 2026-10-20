package dtc

import java.time.LocalDate

import scala.language.implicitConversions

/**
  * An abstraction that allows the concept of "current time" to be injected and controlled
  */
trait Provider[T] extends Serializable {

  /**
    * Current date at specified zone
    */
  def currentDate(zone: TimeZoneId): LocalDate

  /**
    * Current time at specified zone
    */
  def currentTime(zone: TimeZoneId): T
}

object Provider extends Serializable {

  @inline def apply[A](implicit instance: Provider[A]): Provider[A] = instance

  trait Ops[A] extends Serializable {
    type TypeClassType <: Provider[A]
    def self: A
    val typeClassInstance: TypeClassType
  }

  trait ToProviderOps extends Serializable {
    implicit def toProviderOps[A](target: A)(implicit tc: Provider[A]): Ops[A] { type TypeClassType = Provider[A] } =
      new Ops[A] {
        type TypeClassType = Provider[A]
        val self: A = target
        val typeClassInstance: TypeClassType = tc
      }
  }

  object nonInheritedOps extends ToProviderOps

  trait AllOps[A] extends Ops[A] {
    type TypeClassType <: Provider[A]
    val typeClassInstance: TypeClassType
  }

  object ops {
    implicit def toAllProviderOps[A](target: A)(implicit tc: Provider[A]): AllOps[A] { type TypeClassType = Provider[A] } =
      new AllOps[A] {
        type TypeClassType = Provider[A]
        val self: A = target
        val typeClassInstance: TypeClassType = tc
      }
  }
}
