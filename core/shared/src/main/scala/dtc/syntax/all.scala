package dtc.syntax

import dtc.{LawlessDateTimeTC, LocalDateTimeTC, ZonedDateTimeTC}

object all
  extends LocalDateTimeTC.ToLocalDateTimeTCOps
    with ZonedDateTimeTC.ToZonedDateTimeTCOps
    with LawlessDateTimeTC.ToLawlessDateTimeTCOps
