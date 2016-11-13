import java.time.Duration

package object dtc {

  private[dtc] def millisToNanos(millis: Int): Int = millis * NanosInMilli
  private[dtc] def roundNanosToMillis(nanos: Int): Int = (nanos / NanosInMilli) * NanosInMilli
  private[dtc] def truncateToMillis(d: Duration): Duration = d.withNanos(roundNanosToMillis(d.getNano))

  private[dtc] val NanosInMilli: Int = 1000000
  private[dtc] val MillisInSecond: Int = 1000
  private[dtc] val SecondsInMinute: Int = 60
  private[dtc] val MinutesInHour: Int = 60
  private[dtc] val HoursInDay: Int = 24

  private[dtc] val MillisInMinute: Int = MillisInSecond * SecondsInMinute
  private[dtc] val MillisInHour: Int = MillisInMinute * MinutesInHour
  private[dtc] val MillisInDay: Int = MillisInHour * HoursInDay

  private[dtc] val SecondsInDay: Int = SecondsInMinute * MinutesInHour * HoursInDay


  private[dtc] implicit class TimeIntOps(n: Long) {
    def %%(b: Int) = absMod(n, b)
  }
  // % with "time fraction" behaviour: negative numbers are translated to adjacent positive
  // e.g. -3s => 57s
  private[dtc] def absMod(a: Long, b: Int): Int = {
    val m = (a % b).toInt
    if (a >= 0 || m == 0) m
    else b + m
  }
}
