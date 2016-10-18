package dtc

import java.time.{LocalDate, LocalTime}

import cats.kernel.{Eq, PartialOrder}
import org.scalacheck.Prop
import org.scalacheck.Prop.{False, Proof, Result}
import org.scalacheck.util.Pretty

/**
  * Completely copied from cats-kernel-laws.
  * TODO: use laws directly from cats-kernel-laws when it's released against scalacheck 1.13.*
  */
package object laws {
  lazy val proved = Prop(Result(status = Proof))

  lazy val falsified = Prop(Result(status = False))

  object Ops {
    def run[A](sym: String)(lhs: A, rhs: A)(f: (A, A) => Boolean): Prop =
      if (f(lhs, rhs)) proved
      else falsified :| {
        val exp = Pretty.pretty(lhs, Pretty.Params(0))
        val got = Pretty.pretty(rhs, Pretty.Params(0))
        s"($exp $sym $got) failed"
      }
  }

  implicit class CheckEqOps[A](lhs: A)(implicit ev: Eq[A], pp: A => Pretty) {
    def ?==(rhs: A): Prop = Ops.run("?==")(lhs, rhs)(ev.eqv)
    def ?!=(rhs: A): Prop = Ops.run("?!=")(lhs, rhs)(ev.neqv)
  }

  implicit class CheckOrderOps[A](lhs: A)(implicit ev: PartialOrder[A], pp: A => Pretty) {
    def ?<(rhs: A): Prop = Ops.run("?<")(lhs, rhs)(ev.lt)
    def ?<=(rhs: A): Prop = Ops.run("?<=")(lhs, rhs)(ev.lteqv)
    def ?>(rhs: A): Prop = Ops.run("?>")(lhs, rhs)(ev.gt)
    def ?>=(rhs: A): Prop = Ops.run("?>=")(lhs, rhs)(ev.gteqv)
  }

  implicit class BooleanOps[A](lhs: Boolean)(implicit pp: Boolean => Pretty) {
    def ?&&(rhs: Boolean): Prop = Ops.run("?&&")(lhs, rhs)(_ && _)
    def ?||(rhs: Boolean): Prop = Ops.run("?||")(lhs, rhs)(_ || _)
  }

  case class NotChangedValidator[T](before: T, after: T) {
    def apply[P](name: String, props: (T => P)*)(implicit E: Eq[P]): Prop = {
      val falsy = props.filter(prop => E.neqv(prop(before), prop(after)))
      if (falsy.isEmpty) proved
      else falsified :| {
        val b = Pretty.pretty(before, Pretty.Params(0))
        val a = Pretty.pretty(after, Pretty.Params(0))
        s"(Property $name changed. Before: $b, after: $a)"
      }
    }
  }

  def notChanged[T, P](before: T, after: T) = NotChangedValidator(before, after)

  implicit class TimeIntOps(n: Long) {
    def %%(b: Int) = absMod(n, b)
  }
  // % with "time fraction" behaviour: negative numbers are translated to adjacent positive
  // e.g. -3s => 57s
  def absMod(a: Long, b: Int): Int = {
    val m = (a % b).toInt
    if (a >= 0 || m == 0) m
    else b + m
  }

  // eq instances
  implicit val eqLocalTime: Eq[LocalTime] = Eq.instance(_ equals _)
  implicit val eqLocalDate: Eq[LocalDate] = Eq.instance(_ equals _)

}
