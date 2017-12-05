package dtc.cats.instances

import cats.Functor
import dtc.Capture

object constructor extends CatsCaptureInstances

trait CatsCaptureInstances {

  implicit val captureFunctor: Functor[Capture] = new Functor[Capture] {
    def map[A, B](fa: Capture[A])(f: A => B): Capture[B] =
      (date, time, zone) => f(fa.capture(date, time, zone))
  }

}
