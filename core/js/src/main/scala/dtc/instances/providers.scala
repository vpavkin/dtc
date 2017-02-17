package dtc.instances

import java.time.LocalDate

import dtc.js.JSDate
import dtc.{Provider, TimeZoneId}

object providers {

  /**
    * Here we can't do anything about the time-zone, so we just ignore it.
    */
  implicit val realJSDateProvider: Provider[JSDate] = new Provider[JSDate] {
    def currentTime(zone: TimeZoneId): JSDate = JSDate.now
    def currentDate(zone: TimeZoneId): LocalDate = JSDate.now.toLocalDate
  }

}
