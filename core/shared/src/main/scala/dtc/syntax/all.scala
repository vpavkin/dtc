package dtc.syntax

import dtc.{TimePoint, Local, Zoned}

object all extends AllSyntax

trait AllSyntax
  extends Local.ToLocalOps
    with Zoned.ToZonedOps
    with TimePoint.ToTimePointOps
