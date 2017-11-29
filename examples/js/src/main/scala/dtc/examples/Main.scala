package dtc.examples

import java.time.{Duration, LocalDate, LocalTime}

import dtc.instances.jsDate._
import dtc.js.JSDate

import scala.scalajs.js.annotation.JSExportTopLevel

// scalastyle:off
object Main {

  @JSExportTopLevel("dct.examples.Main.main")
  def main() = {
    val calendar = Calendar(List(
      CalendarEvent(
        JSDate.of(LocalDate.now(), LocalTime.of(10, 0)),
        JSDate.of(LocalDate.now(), LocalTime.of(11, 0)),
        "Breakfast"
      ),
      CalendarEvent(
        JSDate.of(LocalDate.now().plusDays(2), LocalTime.of(12, 0)),
        JSDate.of(LocalDate.now().plusDays(2), LocalTime.of(14, 0)),
        "Meeting"
      ),
      CalendarEvent(
        JSDate.of(2016, 10, 9, 11, 0),
        JSDate.of(2016, 10, 9, 11, 0),
        "Birthday party"
      )
    ))

    println(calendar.eventsAfter(JSDate.now).mkString(", "))
    println(calendar.onlyWorkDays.mkString(", "))

    val period = Period(JSDate.now, JSDate.now.plus(Duration.ofDays(1L)))
    println(period.durationInMinutes)
    println(period.durationInSeconds)
    println(period.hours.mkString("\n"))
  }
}
