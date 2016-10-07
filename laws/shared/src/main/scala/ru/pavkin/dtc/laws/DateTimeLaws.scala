package ru.pavkin.dtc.laws

import org.scalacheck.Prop
import ru.pavkin.dtc.DateTime
import Prop.{False, Proof, Result}

/**
  * Laws, that must be obeyed by any ru.pavkin.dtc.DateTime
  */
trait DateTimeLaws[A] {
  implicit def D: DateTime[A]

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

object DateTimeLaws {
  def apply[A](implicit ev: DateTime[A]): DateTimeLaws[A] = new DateTimeLaws[A] {
    def D: DateTime[A] = ev
  }
}
