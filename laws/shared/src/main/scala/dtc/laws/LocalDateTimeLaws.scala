package dtc.laws

import java.time.temporal.{ChronoField, ChronoUnit}
import java.time.{Duration, LocalDate, LocalTime}

import cats.kernel.instances.int._
import cats.kernel.instances.long._
import dtc.LocalDateTimeTC
import dtc.syntax.localDateTime._
import dtc._
import org.scalacheck.{Arbitrary, Gen, Prop}
import org.scalacheck.Prop._

/**
  * Laws, that must be obeyed by any LocalDateTimeTC instance
  */
trait LocalDateTimeLaws[A] {
  implicit def D: LocalDateTimeTC[A]

  val genA: Gen[A]
  val genAdditionSafeDateAndDuration: Gen[(A, Duration)]
  val genLocalDate: Gen[LocalDate]
  val genLocalTime: Gen[LocalTime]
  val genValidYear: Gen[Int]

  lazy val genWithoutFeb29 = genA.suchThat(notFeb29)
  lazy val genWithValidMonthDay = genA.flatMap(a => Gen.choose(1, a.date.lengthOfMonth()).map(a -> _))

  private def notFeb29(x: A) = !(x.dayOfMonth == 29 && x.month == 2)
  private def realSeconds(d: Duration) = {
    val seconds = d.getSeconds
    if (seconds >= 0 || d.getNano == 0) seconds
    else seconds + 1
  }

  def additionAndSubtractionOfSameDuration = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    D.plus(D.plus(x, d), d.negated()) ?== x
  }

  def additionOfZero = forAll(genAdditionSafeDateAndDuration) { case (x, _) =>
    D.plus(x, Duration.ZERO) ?== x
  }

  def additionOfNonZero = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    d.isZero ?||
      ((d.isNegative && D.lt(D.plus(x, d), x)) || D.gt(D.plus(x, d), x))
  }

  def millisAddition = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    D.plus(x, d).millisecond ?== ((x.millisecond + d.toMillis) %% 1000)
  }

  def secondsAddition = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    val seconds = d.toMillis / 1000
    val updated = D.plus(x, Duration.ofSeconds(seconds))
    (updated.second ?== ((x.second + seconds) %% 60)) &&
      notChanged(x, updated)("ms", _.millisecond)
  }

  def minutesAddition = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    val updated = D.plus(x, Duration.ofMinutes(d.toMinutes))
    val validator = notChanged(x, updated)
    (updated.minute ?== ((x.minute + d.toMinutes) %% 60)) &&
      validator("s, ms", _.millisecond, _.second)
  }

  def hoursAddition = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    val updated = D.plus(x, Duration.ofHours(d.toHours))
    val validator = notChanged(x, updated)
    (updated.hour ?== ((x.hour + d.toHours) %% 24)) &&
      validator("m, s, ms", _.minute, _.second, _.millisecond)
  }

  def twoConsequentNowCalls = {
    val prev = D.now
    val current = D.now
    prev ?<= current
  }

  def constructorConsistency = forAll(genLocalDate, genLocalTime) { (date: LocalDate, time: LocalTime) =>
    val dt = D.of(date, time)
    (dt.date ?== date) && (dt.time ?== time.truncatedTo(ChronoUnit.MILLIS))
  }

  def plainConstructorConsistency = forAll(genLocalDate, genLocalTime) { (date: LocalDate, time: LocalTime) =>
    val dt = D.of(
      date.getYear, date.getMonthValue, date.getDayOfMonth,
      time.getHour, time.getMinute, time.getSecond, time.get(ChronoField.MILLI_OF_SECOND))
    (dt.date ?== date) && (dt.time ?== time.truncatedTo(ChronoUnit.MILLIS))
  }

  def withYear = forAll(genWithoutFeb29, genValidYear) { (x: A, year: Int) =>
    val altered = D.withYear(x, year)
    val validator = notChanged(x, altered)
    (altered.year ?== year) &&
      validator("all except year", _.millisecond, _.second, _.minute, _.hour, _.dayOfMonth, _.month)
  }

  def withMonth = forAll(genA, Gen.choose(1, 12)) { (x: A, month: Int) =>
    val altered = D.withMonth(x, month)
    val validator = notChanged(x, altered)
    (altered.month ?== month) &&
      validator("all except month and day", _.millisecond, _.second, _.minute, _.hour, _.year) && (
      (altered.dayOfMonth ?== x.dayOfMonth) || (altered.dayOfMonth ?== altered.date.lengthOfMonth())
      )
  }

  def withDayOfMonth = forAll(genWithValidMonthDay) { case (x, dayOfMonth) =>
    val altered = D.withDayOfMonth(x, dayOfMonth)
    val validator = notChanged(x, altered)
    (altered.dayOfMonth ?== dayOfMonth) &&
      validator("all except day", _.millisecond, _.second, _.minute, _.hour, _.month, _.year)
  }

  def withHour = forAll(genA, Gen.choose(0, 23)) { (x: A, hour: Int) =>
    val altered = D.withHour(x, hour)
    val validator = notChanged(x, altered)
    (altered.hour ?== hour) &&
      validator("all except hour", _.millisecond, _.second, _.minute, _.dayOfMonth, _.month, _.year)
  }

  def withMinute = forAll(genA, Gen.choose(0, 59)) { (x: A, minute: Int) =>
    val altered = D.withMinute(x, minute)
    val validator = notChanged(x, altered)
    (altered.minute ?== minute) &&
      validator("all except minute", _.millisecond, _.second, _.hour, _.dayOfMonth, _.month, _.year)
  }

  def withSecond = forAll(genA, Gen.choose(0, 59)) { (x: A, second: Int) =>
    val altered = D.withSecond(x, second)
    val validator = notChanged(x, altered)
    (altered.second ?== second) &&
      validator("all except second", _.millisecond, _.minute, _.hour, _.dayOfMonth, _.month, _.year)
  }

  def withMillisecond = forAll(genA, Gen.choose(0, 999)) { (x: A, millisecond: Int) =>
    val altered = D.withMillisecond(x, millisecond)
    val validator = notChanged(x, altered)
    (altered.millisecond ?== millisecond) &&
      validator("all except milli", _.second, _.minute, _.hour, _.dayOfMonth, _.month, _.year)
  }

  def untilSelfIsAlwaysZero = forAll(genA) { x: A =>
    (D.millisecondsUntil(x, x) ?== 0L) &&
      (D.secondsUntil(x, x) ?== 0L) &&
      (D.minutesUntil(x, x) ?== 0L) &&
      (D.hoursUntil(x, x) ?== 0L) &&
      (D.daysUntil(x, x) ?== 0L) &&
      (D.monthsUntil(x, x) ?== 0L) &&
      (D.yearsUntil(x, x) ?== 0L)
  }

  def untilIsConsistentWithPlus = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    val altered = D.plus(x, d)
    (D.millisecondsUntil(x, altered) ?== d.toMillis) &&
      (D.secondsUntil(x, altered) ?== realSeconds(d)) &&
      (D.minutesUntil(x, altered) ?== realSeconds(d) / SecondsInMinute) &&
      (D.hoursUntil(x, altered) ?== realSeconds(d) / (SecondsInMinute * MinutesInHour)) &&
      (D.daysUntil(x, altered) ?== d.toDays)
  }

  private def monthsUntilWithLastMonthDateCheck(x: A, y: A, months: Int): Prop =
    x.monthsUntil(y) ?== (
      if (x.dayOfMonth < y.dayOfMonth && months < 0) months + 1
      else if (x.dayOfMonth > y.dayOfMonth && months > 0) months - 1
      else months
      ).toLong

  def monthsUntilIsConsistentWithPlus = forAll(genAdditionSafeDateAndDuration) { case (x, d) =>
    val months = (d.toDays / 31).toInt
    monthsUntilWithLastMonthDateCheck(x, x.plusMonths(months), months)
  }

  def monthsUntilCountsOnlyFullUnits = {
    // scenario that avoids cases like "31.03 monthsUntil 30.06", for which law doesn't hold
    val genScenarioWithoutDateCutoff = (for {
      dateAndDur <- genAdditionSafeDateAndDuration
      fraction <- Gen.choose(1, SecondsInDay * 26)
    } yield {
      val monthFraction = Duration.ofSeconds(
        if (dateAndDur._2.isNegative) -fraction.toLong
        else fraction.toLong
      )
      val months = (dateAndDur._2.minus(monthFraction).toDays / 31).toInt
      (dateAndDur._1, months, monthFraction)
    }).suchThat {
      case (date, months, _) =>
        date.plusMonths(months).dayOfMonth == date.dayOfMonth
    }
    forAll(genScenarioWithoutDateCutoff) { case (date, months, fraction) =>
      date.monthsUntil(date.plusMonths(months).plus(fraction)) ?== months.toLong
    }
  }

  def yearsUntilCountsOnlyFullUnits =
    forAll(genAdditionSafeDateAndDuration.suchThat(pair => notFeb29(pair._1)), Gen.choose(1, SecondsInDay * 354)) {
      (dateAndDur: (A, Duration), yearFractionInSeconds: Int) =>
        val yearFraction = Duration.ofSeconds(
          if (dateAndDur._2.isNegative) -yearFractionInSeconds.toLong
          else yearFractionInSeconds.toLong
        )
        val years = (dateAndDur._2.minus(yearFraction).toDays / (31 * 12)).toInt
        val onlyFullYears = dateAndDur._1.plusYears(years)
        val yearsWithFraction = dateAndDur._1.plusYears(years).plus(yearFraction)

        (dateAndDur._1.yearsUntil(onlyFullYears) ?== years.toLong) &&
          (dateAndDur._1.yearsUntil(yearsWithFraction) ?== years.toLong)
    }
}

object LocalDateTimeLaws {
  def apply[A](
    gDateAndDuration: Gen[(A, Duration)],
    gLocalTime: Gen[LocalTime],
    gLocalDate: Gen[LocalDate],
    gValidYear: Gen[Int])(
    implicit ev: LocalDateTimeTC[A],
    arbA: Arbitrary[A]): LocalDateTimeLaws[A] = new LocalDateTimeLaws[A] {
    def D: LocalDateTimeTC[A] = ev
    val genAdditionSafeDateAndDuration: Gen[(A, Duration)] = gDateAndDuration
    val genLocalDate: Gen[LocalDate] = gLocalDate
    val genLocalTime: Gen[LocalTime] = gLocalTime
    val genValidYear: Gen[Int] = gValidYear
    val genA: Gen[A] = arbA.arbitrary
  }
}
