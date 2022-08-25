package dtc.instances

import java.time.{DayOfWeek, Duration, LocalDate, LocalTime, Period}
import dtc.{Capture, Local, TimeZoneId, Zoned}
import dtc.js.{MomentDateTime, MomentLocalDateTime, MomentZonedDateTime}

package object moment {

  /**
   * This instance uses relaxed time value equality, comparing only underlying instants.
   *
   * Time values are equal when underlying instants are equal. Even if they have different time zones.
   */
  implicit val momentZonedWithCrossZoneEquality: Zoned[MomentZonedDateTime] =
    new MomentZonedDateTimeInstanceWithoutOrder {
      def compare(x: MomentZonedDateTime, y: MomentZonedDateTime): Int = MomentDateTime.compare(x, y)
    }

  /**
   * This instance uses strict time value equality.
   *
   * Time values with different time zones are always considered different,
   * regardless of underlying instant equivalence.
   */
  implicit val momentZonedWithStrictEquality: Zoned[MomentZonedDateTime] =
    new MomentZonedDateTimeInstanceWithoutOrder {
      def compare(x: MomentZonedDateTime, y: MomentZonedDateTime): Int = MomentZonedDateTime.compareStrict(x, y)
    }

  implicit val momentLocalDTC: Local[MomentLocalDateTime] =
    new Local[MomentLocalDateTime] {
      def date(x: MomentLocalDateTime): LocalDate = x.toLocalDate
      def time(x: MomentLocalDateTime): LocalTime = x.toLocalTime

      def plus(x: MomentLocalDateTime, d: Duration): MomentLocalDateTime = x.plus(d)
      def minus(x: MomentLocalDateTime, d: Duration): MomentLocalDateTime = x.minus(d)
      def plusDays(x: MomentLocalDateTime, days: Int): MomentLocalDateTime = x.plusDays(days)
      def plusMonths(x: MomentLocalDateTime, months: Int): MomentLocalDateTime = x.plusMonths(months)
      def plusYears(x: MomentLocalDateTime, years: Int): MomentLocalDateTime = x.plusYears(years)

      def compare(x: MomentLocalDateTime, y: MomentLocalDateTime): Int = MomentDateTime.compare(x, y)

      def of(date: LocalDate, time: LocalTime): MomentLocalDateTime = MomentLocalDateTime.of(date, time)
      def of(
        year: Int, month: Int, day: Int,
        hour: Int, minute: Int, second: Int, millisecond: Int): MomentLocalDateTime =
        MomentLocalDateTime.of(year, month, day, hour, minute, second, millisecond)

      def withYear(x: MomentLocalDateTime, year: Int): MomentLocalDateTime = x.withYear(year)
      def withMonth(x: MomentLocalDateTime, month: Int): MomentLocalDateTime = x.withMonth(month)
      def withDayOfMonth(x: MomentLocalDateTime, dayOfMonth: Int): MomentLocalDateTime = x.withDayOfMonth(dayOfMonth)
      def withHour(x: MomentLocalDateTime, hour: Int): MomentLocalDateTime = x.withHour(hour)
      def withMinute(x: MomentLocalDateTime, minute: Int): MomentLocalDateTime = x.withMinute(minute)
      def withSecond(x: MomentLocalDateTime, second: Int): MomentLocalDateTime = x.withSecond(second)
      def withMillisecond(x: MomentLocalDateTime, millisecond: Int): MomentLocalDateTime =
        x.withMillisecond(millisecond)
      def withTime(x: MomentLocalDateTime, time: LocalTime): MomentLocalDateTime = x.withTime(time)
      def withDate(x: MomentLocalDateTime, date: LocalDate): MomentLocalDateTime = x.withDate(date)


      def dayOfWeek(x: MomentLocalDateTime): DayOfWeek = x.dayOfWeek
      def dayOfMonth(x: MomentLocalDateTime): Int = x.dayOfMonth
      def month(x: MomentLocalDateTime): Int = x.month
      def year(x: MomentLocalDateTime): Int = x.year
      def millisecond(x: MomentLocalDateTime): Int = x.millisecond
      def second(x: MomentLocalDateTime): Int = x.second
      def minute(x: MomentLocalDateTime): Int = x.minute
      def hour(x: MomentLocalDateTime): Int = x.hour

      def yearsUntil(x: MomentLocalDateTime, until: MomentLocalDateTime): Long = x.yearsUntil(until)
      def monthsUntil(x: MomentLocalDateTime, until: MomentLocalDateTime): Long = {
        Period.between(x.toLocalDate, until.toLocalDate).toTotalMonths
        //        x.monthsUntil(until.minus(Duration.ofDays(1)))
      }
      def daysUntil(x: MomentLocalDateTime, until: MomentLocalDateTime): Long = x.daysUntil(until)
      def hoursUntil(x: MomentLocalDateTime, until: MomentLocalDateTime): Long = x.hoursUntil(until)
      def minutesUntil(x: MomentLocalDateTime, until: MomentLocalDateTime): Long = x.minutesUntil(until)
      def secondsUntil(x: MomentLocalDateTime, until: MomentLocalDateTime): Long = x.secondsUntil(until)
      def millisecondsUntil(x: MomentLocalDateTime, until: MomentLocalDateTime): Long = x.millisecondsUntil(until)
    }

  implicit val captureMomentLocalDateTime: Capture[MomentLocalDateTime] =
    (date: LocalDate, time: LocalTime, zone: TimeZoneId) =>
      MomentZonedDateTime.of(date, time, zone).withZoneSameInstant(TimeZoneId.UTC).toLocal

}
