package dtc.laws

import java.time.temporal.{ChronoField, ChronoUnit}
import java.time.{LocalDate, LocalTime}

import cats.kernel.laws._
import cats.kernel.laws.discipline.{catsLawsIsEqToProp => p}
import dtc.Local
import dtc.syntax.local._
import org.scalacheck.{Gen, Prop}
import org.scalacheck.Prop.forAll

/**
  * Laws, that must be obeyed by any Local instance
  */
trait LocalDateTimeLaws[A] {
  implicit def D: Local[A]

  val genLocalDate: Gen[LocalDate]
  val genLocalTime: Gen[LocalTime]

  def constructorConsistency: Prop = forAll(genLocalDate, genLocalTime) { (date: LocalDate, time: LocalTime) =>
    val dt = D.of(date, time)
    p(dt.date <-> date) && (dt.time <-> time.truncatedTo(ChronoUnit.MILLIS))
  }

  def plainConstructorConsistency: Prop = forAll(genLocalDate, genLocalTime) { (date: LocalDate, time: LocalTime) =>
    val dt = D.of(
      date.getYear, date.getMonthValue, date.getDayOfMonth,
      time.getHour, time.getMinute, time.getSecond, time.get(ChronoField.MILLI_OF_SECOND))
    p(dt.date <-> date) && (dt.time <-> time.truncatedTo(ChronoUnit.MILLIS))
  }
}

object LocalDateTimeLaws {
  def apply[A](
    gLocalTime: Gen[LocalTime],
    gLocalDate: Gen[LocalDate])(
    implicit ev: Local[A]): LocalDateTimeLaws[A] = new LocalDateTimeLaws[A] {
    def D: Local[A] = ev
    val genLocalDate: Gen[LocalDate] = gLocalDate
    val genLocalTime: Gen[LocalTime] = gLocalTime
  }
}
