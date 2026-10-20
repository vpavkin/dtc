package dtc

import scala.language.implicitConversions

/**
  * A typeclass for zoned datetime values. These values are zone aware and follow DST transformation rules.
  *
  * Semantics are similar to `java.time.ZonedDateTime`
  */
trait Zoned[A] extends TimePoint[A] with Capture[A] {

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

object Zoned {

  @inline def apply[A](implicit instance: Zoned[A]): Zoned[A] = instance

  trait Ops[A] extends Serializable {
    type TypeClassType <: Zoned[A]
    def self: A
    val typeClassInstance: TypeClassType
    def withZoneSameInstant(zone: TimeZoneId): A = typeClassInstance.withZoneSameInstant(self, zone)
    def withZoneSameLocal(zone: TimeZoneId): A = typeClassInstance.withZoneSameLocal(self, zone)
    def zone: TimeZoneId = typeClassInstance.zone(self)
    def offset: Offset = typeClassInstance.offset(self)
  }

  trait ToZonedOps extends Serializable {
    implicit def toZonedOps[A](target: A)(implicit tc: Zoned[A]): Ops[A] { type TypeClassType = Zoned[A] } =
      new Ops[A] {
        type TypeClassType = Zoned[A]
        val self: A = target
        val typeClassInstance: TypeClassType = tc
      }
  }

  object nonInheritedOps extends ToZonedOps

  trait AllOps[A] extends Ops[A] with TimePoint.AllOps[A] with Capture.AllOps[A] {
    type TypeClassType <: Zoned[A]
    val typeClassInstance: TypeClassType
  }

  object ops {
    implicit def toAllZonedOps[A](target: A)(implicit tc: Zoned[A]): AllOps[A] { type TypeClassType = Zoned[A] } =
      new AllOps[A] {
        type TypeClassType = Zoned[A]
        val self: A = target
        val typeClassInstance: TypeClassType = tc
      }
  }
}
