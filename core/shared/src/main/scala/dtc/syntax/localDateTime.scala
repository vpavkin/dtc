package dtc.syntax

import dtc.{LawlessDateTimeTC, LocalDateTimeTC}

object localDateTime
  extends LocalDateTimeTC.ToLocalDateTimeTCOps
    with LawlessDateTimeTC.ToLawlessDateTimeTCOps
