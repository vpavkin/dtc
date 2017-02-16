package dtc.examples

import java.time.Duration

import dtc.Local
import dtc.syntax.local._

import scala.annotation.tailrec

case class Period[T: Local](start: T, end: T) {

  @tailrec
  private def iterateByLoop(accumulator: List[T], duration: Duration): List[T] = {
    val next = accumulator.head.plus(duration)
    if (next isAfterOrEquals end) accumulator
    else iterateByLoop(next :: accumulator, duration)
  }

  def iterateFromStartBy(duration: Duration): List[T] =
    iterateByLoop(List(start), duration).reverse

  def prolong(by: Duration): Period[T] = copy(end = end.plus(by))

  def durationInSeconds: Long = start.secondsUntil(end)
  def durationInMinutes: Long = start.minutesUntil(end)

  def hours: List[T] = iterateFromStartBy(Duration.ofHours(1L))
}
