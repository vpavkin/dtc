package ru.pavkin.dtc

import java.time.LocalDateTime

import ru.pavkin.dtc.instances.localDateTime._
import com.fortysevendeg.scalacheck.datetime.jdk8.ArbitraryJdk8._
import org.scalacheck.Arbitrary
import ru.pavkin.dtc.laws.LocalDateTimeTCTests

class LocalDateTimeTests extends DTCSuite {
  implicit val arbLocalDateTime: Arbitrary[LocalDateTime] = Arbitrary(genZonedDateTime.map(_.toLocalDateTime))

  checkAll("DateTime[java.time.LocalDateTime]", LocalDateTimeTCTests[LocalDateTime].localDateTime)
}

