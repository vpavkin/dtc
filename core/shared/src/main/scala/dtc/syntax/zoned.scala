package dtc.syntax

import dtc.{Lawless, Zoned}

object zoned
  extends Zoned.ToZonedOps
    with Lawless.ToLawlessOps
