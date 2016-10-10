package dtc.tests

import java.time.{LocalDateTime, ZoneOffset}

import com.fortysevendeg.scalacheck.datetime.jdk8.ArbitraryJdk8._
import dtc.instances.localDateTime._
import dtc.laws.{LocalDateTimeTCTests, OrderLaws}
import org.scalacheck.{Arbitrary, Cogen}

// todo: Add Order laws when cats-kernel-laws are released against scalacheck 1.13.*
class LocalDateTimeTests extends ExtendedSyntaxTests[LocalDateTime] {

  implicit val arbT: Arbitrary[LocalDateTime] = Arbitrary(genZonedDateTime.map(_.toLocalDateTime))
  implicit val cogenT: Cogen[LocalDateTime] = Cogen(_.toEpochSecond(ZoneOffset.UTC))

  checkAll("java.time.LocalDateTime", LocalDateTimeTCTests[LocalDateTime].localDateTime)
  checkAll("java.time.LocalDateTime", OrderLaws[LocalDateTime].order)
  checkAll("java.time.LocalDateTime", OrderLaws[LocalDateTime].partialOrder)
  checkAll("java.time.LocalDateTime", OrderLaws[LocalDateTime].eqv)
}

