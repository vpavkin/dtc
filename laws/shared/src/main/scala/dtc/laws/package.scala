package dtc

import java.time.{DayOfWeek, LocalDate, LocalTime}

import cats.kernel.Eq
import org.scalacheck.Prop
import org.scalacheck.Prop._
import org.scalacheck.util.Pretty

package object laws {

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

  // eq instances
  implicit val eqLocalTime: Eq[LocalTime] = Eq.fromUniversalEquals
  implicit val eqLocalDate: Eq[LocalDate] = Eq.fromUniversalEquals
  implicit val eqDayOfWeek: Eq[DayOfWeek] = Eq.fromUniversalEquals
}
