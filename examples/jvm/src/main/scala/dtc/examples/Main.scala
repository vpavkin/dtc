package dtc.examples

import java.time.{LocalDate, LocalDateTime, LocalTime, Month}

import dtc.instances.localDateTime._

// scalastyle:off
object Main extends App {

  val calendar = Calendar(List(
    CalendarEvent(
      LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)),
      LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0)),
      "Breakfast"
    ),
    CalendarEvent(
      LocalDateTime.of(LocalDate.now().minusDays(2), LocalTime.of(12, 0)),
      LocalDateTime.of(LocalDate.now().minusDays(2), LocalTime.of(14, 0)),
      "Meeting"
    ),
    CalendarEvent(
      LocalDateTime.of(2016, Month.OCTOBER, 9, 11, 0),
      LocalDateTime.of(2016, Month.OCTOBER, 9, 11, 0),
      "Birthday party"
    )
  ))

  println(calendar.eventsAfter(LocalDateTime.now().minusDays(1L)).mkString(", "))
  println(calendar.onlyWorkDays.mkString(", "))
}
