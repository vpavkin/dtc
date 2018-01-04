package dtc.tests

import java.time.{Duration, LocalDateTime, ZoneOffset}

import cats.instances.option._
import cats.kernel.laws.discipline.OrderTests
import com.fortysevendeg.scalacheck.datetime.jdk8.ArbitraryJdk8._
import dtc.instances.localDateTime._
import dtc.laws.{DateTimeTests, LocalDateTimeTests, ProviderTests}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Cogen}
import dtc.instances.providers.realLocalDateTimeProvider

class JVMLocalDateTimeTests extends DTCSuiteJVM {

  implicit val arbT: Arbitrary[LocalDateTime] = Arbitrary(genZonedDateTime.map(_.toLocalDateTime))
  implicit val cogenT: Cogen[LocalDateTime] = Cogen(_.toEpochSecond(ZoneOffset.UTC))

  val overflowSafePairGen = for {
    dt <- arbitrary[LocalDateTime]
    dur <- arbitrary[Duration]
  } yield (dt, dur)

  val ldtTests = LocalDateTimeTests[LocalDateTime](overflowSafePairGen, genYear)
  checkAll("java.time.LocalDateTime", DateTimeTests[LocalDateTime](overflowSafePairGen).dateTime)
  checkAll("java.time.LocalDateTime", ldtTests.localDateTime)
  checkAll("java.time.LocalDateTime", ldtTests.monthUntilFractionHandling)
  checkAll("java.time.LocalDateTime", OrderTests[LocalDateTime].order)
  checkAll("java.time.LocalDateTime", OrderTests[LocalDateTime].partialOrder)
  checkAll("java.time.LocalDateTime", OrderTests[LocalDateTime].eqv)

  checkAll("java.time.LocalDateTime", ProviderTests[LocalDateTime](genTimeZone).provider)
}

