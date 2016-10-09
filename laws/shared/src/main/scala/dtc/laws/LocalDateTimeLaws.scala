package dtc.laws

import dtc.LocalDateTimeTC
import org.scalacheck.Prop
import org.scalacheck.Prop.{False, Proof, Result}

/**
  * Laws, that must be obeyed by any LocalDateTimeTC
  */
trait LocalDateTimeLaws[A] {
  implicit def D: LocalDateTimeTC[A]

  val proved = Prop(Result(status = Proof))
  val falsified = Prop(Result(status = False))

  def dateMustNotThrow(x: A): Prop = {
    D.date(x)
    proved
  }

  def timeMustNotThrow(x: A): Prop = {
    D.time(x)
    proved
  }

}

object LocalDateTimeLaws {
  def apply[A](implicit ev: LocalDateTimeTC[A]): LocalDateTimeLaws[A] = new LocalDateTimeLaws[A] {
    def D: LocalDateTimeTC[A] = ev
  }
}
