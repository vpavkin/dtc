# DTC (Datetime Type Class)
### Type classes for datetime values. Works both on JVM & ScalaJS.

[![Build Status](https://img.shields.io/travis/vpavkin/dtc/master.svg)](https://travis-ci.org/vpavkin/dtc) 
[![Coverage status](https://img.shields.io/codecov/c/github/vpavkin/dtc/master.svg)](https://codecov.io/github/vpavkin/dtc?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/ru.pavkin/dtc-core_2.11.svg)](https://github.com/vpavkin/dtc)
[![Join the chat at https://gitter.im/vpavkin/dtc](https://badges.gitter.im/vpavkin/dtc.svg)](https://gitter.im/vpavkin/dtc?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

DTC provides type classes for local and zoned datetime values, and type class instances for both JVM and ScalaJS.

It serves 2 main purposes:

1) **Allows to write generic polymorphic code, that operates on datetime values.**
2) **Gives possibility to write universal datetime logic, that compiles both for JVM and ScalaJS.** 
Currently, there's no truly cross-platform datetime instance, as [scala-js-java-time](https://github.com/scala-js/scala-js-java-time) does not yet provide `java.time.LocalDateTime` and `java.time.ZonedDateTime`.

As a side-effect, you get immutable datetime values for ScalaJS that behave like `java.time._` counterparts.

DTC core depends on:
- [scala-js-java-time](https://github.com/scala-js/scala-js-java-time) to take advantage of `java.time._` API parts, that are already available for ScalaJS
- small [cats-kernel](https://github.com/typelevel/cats) module for `Order` type class.

1. [Usage](#usage)
  1. [Setup](#setup)
  2. [Simple example](#simple-example)
  3. [Type classes](#type-classes)
    1. [LawlessDateTimeTC](#LawlessDateTimeTC)
    1. [LocalDateTimeTC](#LocalDateTimeTC)
    2. [ZonedDateTimeTC](#ZonedDateTimeTC)
  4. [Instances](#instances)
    1. [JVM instances](#jvm-instances)
    2. [JS instances](#js-instances)
  5. [Syntax and Cats integration](#syntax-and-cats-integration)
2. [Motivation](#motivation)
3. [Modules](#modules)
4. [Known issues](#known-issues)


## Usage

### Setup
Add this line to your `build.sbt`.

```scala
libraryDependencies += "ru.pavkin" %%% "dtc-core" % "0.4.0"
```

If you want to use momentjs instances for ScalaJS runtime (see [JS instances](#js-instances)), also add `moment` module dependency to your scalajs subproject as well:
```scala
libraryDependencies += "ru.pavkin" %%% "dtc-moment" % "0.4.0"
```

### Simple example

Let's create a simple polymorphic class that works with local datetime values.

Say we want a period entity that operates on local datetime values and knows both it's start and end:

```scala
import java.time.Duration // this is provided crossplatformly by scala-js-java-time

import dtc.LocalDateTimeTC
import dtc.syntax.localDateTime._ // syntax extensions for LocalDateTimeTC instances

case class Period[T: LocalDateTimeTC](start: T, end: T) {

  def prolong(by: Duration): Period[T] = copy(end = end.plus(by)) // syntax in action

  def durationInSeconds: Long = start.secondsUntil(end) // syntax in action
  def durationInMinutes: Long = start.minutesUntil(end) // syntax in action
}
```

It is 100% cross-platform, so we can put it into a "shared" cross-project, to use later on both platforms.

Let's create two simple apps to demostrate the concept.

First, let's start with JVM:

```scala
import java.time.LocalDateTime

import dtc.instances.localDateTime._ // provide implicit typeclass instance for java.time.LocalDateTime
import dtc.examples.Period

object Main extends App {

  // with implicit typeclass instance in scope we can put LocalDateTime instances here.
  val period = Period(LocalDateTime.now(), LocalDateTime.now().plusDays(1L))
  println(period.durationInMinutes)
  println(period.durationInSeconds)
  println(period.hours.mkString("\n"))
}
```

Next, nothing stops us from creating a JS app as well

```scala
import java.time.{Duration, LocalDate, LocalTime}

import dtc.js.JSDate // this is special wrapper around plain JS date, that provides basic FP guarantees, e.g. immutability
import dtc.instances.jsDate._ // implicit LocalDateTimeTC instance for JSDate
import dtc.examples.Period

import scala.scalajs.js.JSApp

object Main extends JSApp {

  def main() = {

    val period = Period(JSDate.now, JSDate.now.plus(Duration.ofDays(1L)))
    println(period.durationInMinutes)
    println(period.durationInSeconds)
    println(period.hours.mkString("\n"))
  }
}
```
---
This examples demonstrates the core idea of the project. 
Read further to check out the list of available type classes and instances. 

### Type classes

*Disclaimer*: although following entities are called type classes, there are not "pure". 
For example, they can throw exceptions for invalid method parameters. This is intentional:
**primary goal is to provide API that looks like java.time._ as much as it's possible.**

DTC provides 3 type classes.

#### `LawlessDateTimeTC`

`LawlessDateTimeTC extends cats.kernel.Order` ([api](https://github.com/vpavkin/dtc/blob/master/core/shared/src/main/scala/dtc/LawlessDateTimeTC.scala))

Base type class, that can be used for both local and zoned datetime instances.

All instances in DTC are extending `LawlessDateTimeTC`
Most of the APIs are same for any datetime value, so with this typeclass you get:
  - all common methods and syntax except ones that are specific to local or zoned datetime values (e.g. constructors)
  - you can use both zoned and local datetime instances to fill in the type parameter (not simultaneously, of course)
  - almost no laws within the polymorphic code context :)

#### `LocalDateTimeTC`

`LocalDateTimeTC extends LawlessDateTimeTC` ([api](https://github.com/vpavkin/dtc/blob/master/core/shared/src/main/scala/dtc/LocalDateTimeTC.scala))

Type class for values, that behave similarly to `java.time.LocalDateTime`. Instances hold local datetime laws.

#### `ZonedDateTimeTC`

`ZonedDateTimeTC extends LawlessDateTimeTC` ([api](https://github.com/vpavkin/dtc/blob/master/core/shared/src/main/scala/dtc/ZonedDateTimeTC.scala))

Type class for values, that behave similarly to `java.time.ZonedDateTime`. Instances hold zoned datetime laws.

### Instances

To make your polymorphic code work on a specific platform, you'll need to supply typeclass instances for concrete datetime types you use.

DTC provides instances for both JVM and ScalaJS.

#### JVM instances

For JVM everything is straightforward:

1. `java.time.LocalDateTime` has an instance of `LocalDateTimeTC`.
2. `java.time.ZonedDateTime` has an instance of `ZonedDateTimeTC`.

To get the instances, just import

```scala
import dtc.instances.localDateTime._
// or
import dtc.instances.zonedDateTime._
```
respectively.

#### JS instances

First of all, DTC **does not provide** instances for **raw** js values (neither `Date` nor `moment`). 
They are mess to work with directly for two reasons:
1. They are mutable
2. They have totally different semantics, comparing to `java.time._`.

Instead, DTC provides simple wrappers that delegate to underlying values (or even enrich the provided API).
These wrappers provide immutability guarantees and adapt the behaviour to follow `java.time._` semantics.

For ease of direct use, they reflect typeclass API as much as possible. 
Though, amount of actual direct use of them should be naturally very limited, because, well... you can write polymorphic code instead!

##### `JSDate`

`JSDate` wraps native ECMA-Script Date and provides instance for `LocalDateTimeTC`.

Instance can be imported like this:
```scala
import dtc.instances.jsDate._
```

Javascript date has a very limited API which doesn't allow to handle time zones in a proper way. So there's no `ZonedDateTimeTC` instance for it.

If you need a `ZonedDateTimeTC` instance, take a look at moment submodule, which is following next.

##### `MomentLocalDateTime` and `MomentZonedDateTime`

These are based on popular [MomentJS](http://momentjs.com/) javascript library as well as [ScalaJS facade](https://github.com/vpavkin/scala-js-momentjs) for it.

To add them to your project, you'll need an explicit dependency on `dtc-moment` module:
```scala
libraryDependencies += "ru.pavkin" %%% "dtc-moment" % "0.4.0"
```

Both classes wrap `moment.Date` and, as you can guess:
- `MomentLocalDateTime` has a `LocalDateTimeTC` instance, and
- `MomentZonedDateTime` has a `ZonedDateTimeTC` instance

You can get both instances in scope by adding:
```scala
import dtc.instances.moment._
```

### Syntax and Cats integration

#### DTC syntax

When writing polymorphic code with DTC, it's very convenient to use syntax extensions. 
They are similar to what Cats or ScalaZ provide for their type classes.

Just add following to your imports:
```scala
import dtc.syntax.localDateTime._  // for LocalDateTimeTC syntax
// or
import dtc.syntax.zonedDateTime._  // for ZonedlDateTimeTC syntax
```

If you need syntax for `LawlessDateTimeTC` or for both local and zoned type classes in the same file, 
just import all syntax at once:
```scala
import dtc.syntax.all._
```

#### `cats.kernel.Order` syntax.

Though, DTC provides basic API for datetime values comparison, it's more convenient and readable to use operators like `<`, `>=` and alike.
To pull this off, you will need syntax extensions for `cats.kernel.Order`, that is extended by all DTC type classes.

Unfortunately, kernel doesn't have syntax extensions. 
So to get this syntax, you'll need to add explicit `cats-core` dependency to your project:
```scala
libraryDependencies += "org.typelevel" %%% "cats-core" % "0.7.2"
```

After that just add regular cats import to get `Order` syntax for datetime values:
```scala
import cats.syntax.order._
```

## Motivation
TBD.

## Modules

DTC modules with published artifacts:
1. `dtc-core` - all type classes and instances for `java.time._` and `JSDate`
2. `dtc-moment` (ScalaJS only) - momentjs instances
3. `dtc-laws` - [discipline](https://github.com/typelevel/discipline) laws to test your own instances


## Known issues

#### MomentJS #3029
There's an open longstanding [bug](https://github.com/moment/moment/issues/3029) in MomentJS. 
In some rare cases it gives incorrect diffs for `monthsUntil` method.

As of current version of DTC, this bug leaks into momentjs instances as well.





