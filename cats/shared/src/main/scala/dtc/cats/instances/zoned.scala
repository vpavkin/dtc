package dtc.cats.instances

import java.time.{DayOfWeek, Duration, LocalDate, LocalTime}

import cats.functor.Invariant
import dtc.{Offset, TimeZoneId, Zoned}

object zoned extends CatsZonedInstances

trait CatsZonedInstances {
  implicit val zonedInvariant: Invariant[Zoned] = new Invariant[Zoned] {
    def imap[A, B](ev: Zoned[A])(f: A => B)(g: B => A): Zoned[B] = new Zoned[B] {
      def compare(x: B, y: B): Int = ev.compare(g(x), g(y))
      def zone(t: B): TimeZoneId = ev.zone(g(t))
      def millisecond(x: B): Int = ev.millisecond(g(x))
      def second(t: B): Int = ev.second(g(t))
      def minute(t: B): Int = ev.minute(g(t))
      def hour(t: B): Int = ev.hour(g(t))
      def dayOfMonth(t: B): Int = ev.dayOfMonth(g(t))
      def dayOfWeek(x: B): DayOfWeek = ev.dayOfWeek(g(x))
      def month(t: B): Int = ev.month(g(t))
      def year(t: B): Int = ev.year(g(t))
      def of(date: LocalDate, time: LocalTime, zone: TimeZoneId): B = f(ev.of(date, time, zone))
      def withZoneSameInstant(x: B, zone: TimeZoneId): B = f(ev.withZoneSameInstant(g(x), zone))
      def withZoneSameLocal(x: B, zone: TimeZoneId): B = f(ev.withZoneSameLocal(g(x), zone))
      def offset(x: B): Offset = ev.offset(g(x))
      def date(x: B): LocalDate = ev.date(g(x))
      def time(x: B): LocalTime = ev.time(g(x))
      def plus(x: B, d: Duration): B = f(ev.plus(g(x), d))
      def minus(x: B, d: Duration): B = f(ev.minus(g(x), d))
      def plusDays(x: B, days: Int): B = f(ev.plusDays(g(x), days))
      def plusMonths(x: B, months: Int): B = f(ev.plusMonths(g(x), months))
      def plusYears(x: B, years: Int): B = f(ev.plusYears(g(x), years))
      def withYear(x: B, year: Int): B = f(ev.withYear(g(x), year))
      def withMonth(x: B, month: Int): B = f(ev.withMonth(g(x), month))
      def withDayOfMonth(x: B, dayOfMonth: Int): B = f(ev.withDayOfMonth(g(x), dayOfMonth))
      def withHour(x: B, hour: Int): B = f(ev.withHour(g(x), hour))
      def withMinute(x: B, minute: Int): B = f(ev.withMinute(g(x), minute))
      def withSecond(x: B, second: Int): B = f(ev.withSecond(g(x), second))
      def withMillisecond(x: B, millisecond: Int): B = f(ev.withMillisecond(g(x), millisecond))
      def yearsUntil(x: B, until: B): Long = ev.yearsUntil(g(x), g(until))
      def monthsUntil(x: B, until: B): Long = ev.monthsUntil(g(x), g(until))
      def daysUntil(x: B, until: B): Long = ev.daysUntil(g(x), g(until))
      def hoursUntil(x: B, until: B): Long = ev.hoursUntil(g(x), g(until))
      def minutesUntil(x: B, until: B): Long = ev.minutesUntil(g(x), g(until))
      def secondsUntil(x: B, until: B): Long = ev.secondsUntil(g(x), g(until))
      def millisecondsUntil(x: B, until: B): Long = ev.millisecondsUntil(g(x), g(until))
    }
  }
}
