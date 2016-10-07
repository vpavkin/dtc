package ru.pavkin.dtc

import java.time.LocalDateTime

import ru.pavkin.dtc.instances.localDateTime._
import ru.pavkin.dtc.laws.DateTimeTests
import com.fortysevendeg.scalacheck.datetime.jdk8.ArbitraryJdk8._
import org.scalacheck.Arbitrary

class LocalDateTimeTests extends DTCSuite {
  implicit val arbLocalDateTime: Arbitrary[LocalDateTime] = Arbitrary(genZonedDateTime.map(_.toLocalDateTime))

  checkAll("DateTime[java.time.LocalDateTime]", DateTimeTests[LocalDateTime].dateTime)
}

