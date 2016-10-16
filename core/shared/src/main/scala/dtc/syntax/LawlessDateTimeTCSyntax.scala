package dtc.syntax

import java.time.DayOfWeek
import java.time.temporal.ChronoField

import dtc.LawlessDateTimeTC

import scala.language.implicitConversions

trait ToLawlessDateTimeTCExtendedOps {
  implicit def toLawlessDateTimeTCExtendedOps[A](x: A)(implicit tc: LawlessDateTimeTC[A]) =
    new LawlessDateTimeTCExtendedOps(x)
}

final class LawlessDateTimeTCExtendedOps[A](x: A)(implicit DT: LawlessDateTimeTC[A]) {
  def dayOfWeek: DayOfWeek = DT.date(x).getDayOfWeek
  def dayOfMonth: Int = DT.date(x).getDayOfMonth
  def month: Int = DT.date(x).getMonthValue
  def year: Int = DT.date(x).getYear
  def millisecond: Int = DT.time(x).get(ChronoField.MILLI_OF_SECOND)
  def second: Int = DT.time(x).getSecond
  def minute: Int = DT.time(x).getMinute
  def hour: Int = DT.time(x).getHour
}
