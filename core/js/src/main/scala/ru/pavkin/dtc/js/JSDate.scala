package ru.pavkin.dtc.js

import java.time.temporal.ChronoField
import java.time.{LocalDate, LocalTime}

import scala.scalajs.js.Date
import scala.util.Try

/**
  * Mutability safe wrapper around plain JS Date.
  *
  * Shouldn't be used as a standalone thing as API is just enough
  * to fit [[ru.pavkin.dtc.LocalDateTimeTC]] typeclass requirements.
  *
  * Supports only [[ru.pavkin.dtc.LocalDateTimeTC]] typeclass due to weak time-zone capabilities.
  */
class JSDate private(private val underlying: Date) {

  private def updated(modifier: Date => Date): JSDate =
    new JSDate(modifier(new Date(underlying.getTime())))

  def dayOfMonth: Int = underlying.getDate()
  def month: Int = underlying.getMonth() + 1
  def year: Int = underlying.getFullYear()
  def hour: Int = underlying.getHours()
  def minute: Int = underlying.getMinutes()
  def second: Int = underlying.getSeconds()

  def toLocalDate: LocalDate = LocalDate.of(year, month, dayOfMonth)
  def toLocalTime: LocalTime = LocalTime.of(hour, minute, second)

  override def toString = underlying.toString
}

object JSDate {

  def of(year: Int, month: Int, day: Int, hour: Int, minute: Int = 0, second: Int = 0, millisecond: Int = 0): JSDate = {
    val date = Try(LocalDate.of(year, month, day))
    require(date.isSuccess, s"Invalid date: ${date.failed.get.getMessage}")

    val time = Try(LocalTime.of(hour, minute, second, millisecond * 1000))
    require(time.isSuccess, s"Invalid time: ${time.failed.get.getMessage}")

    of(date.get, time.get)
  }


  def of(date: LocalDate, time: LocalTime): JSDate = new JSDate(new Date(Date.UTC(
    date.getYear, date.getMonthValue - 1, date.getDayOfMonth,
    time.getHour, time.getMinute, time.getSecond, time.get(ChronoField.MILLI_OF_SECOND)
  )))
}
