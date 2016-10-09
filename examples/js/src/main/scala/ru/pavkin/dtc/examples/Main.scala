package ru.pavkin.dtc.examples

import java.time.{LocalDate, LocalTime}

import ru.pavkin.dtc.js.JSDate
import ru.pavkin.dtc.instances.jsDate._

import scala.scalajs.js.JSApp

// scalastyle:off
object Main extends JSApp {

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
  }
}
