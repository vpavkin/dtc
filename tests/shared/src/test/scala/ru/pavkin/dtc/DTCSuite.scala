package ru.pavkin.dtc

import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

abstract class DTCSuite extends FunSuite
  with Matchers
  with GeneratorDrivenPropertyChecks
  with Discipline
