package dtc.tests

import org.scalatest.FunSuiteLike
import org.scalatestplus.scalacheck.Checkers
import org.typelevel.discipline.Laws

trait Discipline extends Checkers {
  self: FunSuiteLike =>

  def checkAll(name: String, ruleSet: Laws#RuleSet): Unit = {
    for ((id, prop) <- ruleSet.all.properties)
      test(name + "." + id) {
        check(prop)
      }
  }

}