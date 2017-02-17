package dtc.syntax

import dtc.{Lawless, Local}

object local
  extends Local.ToLocalOps
    with Lawless.ToLawlessOps
