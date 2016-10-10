package dtc.laws

import dtc.LawlessDateTimeTC
import org.scalacheck.Prop

/**
  * Laws, that must be obeyed by any DateTime typeclass
  */
trait DateTimeLaws[A] {
  implicit def D: LawlessDateTimeTC[A]

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
  def apply[A](implicit ev: LawlessDateTimeTC[A]): DateTimeLaws[A] = new DateTimeLaws[A] {
    def D: LawlessDateTimeTC[A] = ev
  }
}
