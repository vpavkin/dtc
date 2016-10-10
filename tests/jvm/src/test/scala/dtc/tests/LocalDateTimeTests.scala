package dtc.tests

import java.time.{LocalDateTime, ZoneOffset}

import com.fortysevendeg.scalacheck.datetime.jdk8.ArbitraryJdk8._
import dtc.instances.localDateTime._
import dtc.laws.{DateTimeTCTests, OrderLaws}
import org.scalacheck.{Arbitrary, Cogen}

class LocalDateTimeTests extends ExtendedSyntaxTests[LocalDateTime] {

  implicit val arbT: Arbitrary[LocalDateTime] = Arbitrary(genZonedDateTime.map(_.toLocalDateTime))
  implicit val cogenT: Cogen[LocalDateTime] = Cogen(_.toEpochSecond(ZoneOffset.UTC))

  checkAll("java.time.LocalDateTime", DateTimeTCTests[LocalDateTime].dateTime)
  checkAll("java.time.LocalDateTime", OrderLaws[LocalDateTime].order)
  checkAll("java.time.LocalDateTime", OrderLaws[LocalDateTime].partialOrder)
  checkAll("java.time.LocalDateTime", OrderLaws[LocalDateTime].eqv)
}

