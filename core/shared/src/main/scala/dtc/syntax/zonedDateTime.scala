package dtc.syntax

import dtc.{LawlessDateTimeTC, ZonedDateTimeTC}

object zonedDateTime
  extends ZonedDateTimeTC.ToZonedDateTimeTCOps
    with LawlessDateTimeTC.ToLawlessDateTimeTCOps
