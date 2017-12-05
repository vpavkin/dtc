package dtc.syntax

import dtc.{TimePoint, Zoned}

object zoned
  extends Zoned.ToZonedOps
    with TimePoint.ToTimePointOps
