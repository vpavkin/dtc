package dtc

import java.time.LocalDateTime

import com.fortysevendeg.scalacheck.datetime.jdk8.ArbitraryJdk8._
import dtc.instances.localDateTime._
import dtc.laws.LocalDateTimeTCTests
import org.scalacheck.Arbitrary

// todo: Add Order laws when cats-kernel-laws are released against scalacheck 1.13.*
class LocalDateTimeTests extends DTCSuite {
  implicit val arbLocalDateTime: Arbitrary[LocalDateTime] = Arbitrary(genZonedDateTime.map(_.toLocalDateTime))

  checkAll("java.time.LocalDateTime", LocalDateTimeTCTests[LocalDateTime].localDateTime)
}

