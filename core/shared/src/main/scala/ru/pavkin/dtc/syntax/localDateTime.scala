package ru.pavkin.dtc.syntax

import ru.pavkin.dtc.{LawlessDateTimeTC, LocalDateTimeTC}

object localDateTime extends LocalDateTimeTC.ToLocalDateTimeTCOps with LawlessDateTimeTC.ToLawlessDateTimeTCOps
