package dtc.syntax

import dtc.{TimePoint, Local}

object local
  extends Local.ToLocalOps
    with TimePoint.ToTimePointOps
