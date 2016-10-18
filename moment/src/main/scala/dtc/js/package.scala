package dtc

import java.time.{LocalDate, LocalTime}
import java.time.temporal.ChronoField

import org.widok.moment.{Date, Moment}

import scala.scalajs.js.Array

package object js {

  private[js] def utcMoment(date: LocalDate, time: LocalTime): Date = Moment.utc(Array(
    date.getYear, date.getMonthValue - 1, date.getDayOfMonth,
    time.getHour, time.getMinute, time.getSecond, time.get(ChronoField.MILLI_OF_SECOND)
  ))
}
