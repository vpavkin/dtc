package dtc.laws

import dtc.LocalDateTimeTC
import org.scalacheck.Prop

/**
  * Laws, that must be obeyed by any LocalDateTimeTC
  */
trait LocalDateTimeLaws[A] {
  implicit def D: LocalDateTimeTC[A]

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
