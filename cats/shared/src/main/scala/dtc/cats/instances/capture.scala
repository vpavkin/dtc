package dtc.cats.instances

import java.time.{LocalDate, LocalTime}

import cats.Functor
import dtc.{Capture, TimeZoneId}

object constructor extends CatsCaptureInstances

trait CatsCaptureInstances {

  implicit val captureFunctor: Functor[Capture] = new Functor[Capture] {
    def map[A, B](fa: Capture[A])(f: A => B): Capture[B] = new Capture[B] {
      def capture(date: LocalDate, time: LocalTime, zone: TimeZoneId): B = f(fa.capture(date, time, zone))
    }
  }

}
