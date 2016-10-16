
package object dtc {

  private[dtc] def millisToNanos(millis: Int): Int = millis * NanosInMilli

  private[dtc] val NanosInMilli: Int = 1000000
  private[dtc] val MillisInSecond: Int = 1000
  private[dtc] val SecondsInMinute: Int = 60
  private[dtc] val MinutesInHour: Int = 60
  private[dtc] val HoursInDay: Int = 24

  private[dtc] val MillisInMinute: Int = MillisInSecond * SecondsInMinute
  private[dtc] val MillisInHour: Int = MillisInMinute * MinutesInHour
  private[dtc] val MillisInDay: Int = MillisInHour * HoursInDay
}
