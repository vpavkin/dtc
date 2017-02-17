package dtc.cats

import dtc.{Local, Zoned}
import dtc.cats.instances.all._

package object syntax {

  final implicit class ZonedObjOps(val o: Zoned.type) extends AnyVal {
    /** Helper to create new Zoned based on existing one (using invariant instance) */
    def by[A, B](f: A => B)(g: B => A)(implicit ev: Zoned[A]): Zoned[B] = zonedInvariant.imap(ev)(f)(g)
  }

  final implicit class LocalObjOps(val o: Local.type) extends AnyVal {
    /** Helper to create new Local based on existing one (using invariant instance) */
    def by[A, B](f: A => B)(g: B => A)(implicit ev: Local[A]): Local[B] = localInvariant.imap(ev)(f)(g)
  }
}
