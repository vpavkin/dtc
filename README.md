# DTC (Datetime Type Classes)
### Type classes for datetime values. Works both on JVM & ScalaJS.

[![Build Status](https://img.shields.io/travis/vpavkin/dtc/master.svg)](https://travis-ci.org/vpavkin/dtc) 
[![Coverage status](https://img.shields.io/codecov/c/github/vpavkin/dtc/master.svg)](https://codecov.io/github/vpavkin/dtc?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/ru.pavkin/dtc-core_2.11.svg)](https://github.com/vpavkin/dtc)
[![Join the chat at https://gitter.im/datetime-type-classes/dtc](https://badges.gitter.im/vpavkin/dtc.svg)](https://gitter.im/datetime-type-classes/dtc?utm_source=share-link&utm_medium=link&utm_campaign=share-link)

DTC provides type classes for local and zoned datetime values, and type class instances for both JVM and ScalaJS.

It serves 2 main purposes:

1. **Allows to write generic polymorphic code, that operates on datetime values.**
2. **Gives possibility to write universal datetime logic, that compiles both for JVM and ScalaJS.** 
Currently, there's no truly cross-platform datetime instance, as [scala-js-java-time](https://github.com/scala-js/scala-js-java-time) does not yet provide `java.time.LocalDateTime` and `java.time.ZonedDateTime`.

As a bonus, you get immutable datetime values for ScalaJS that behave like `java.time._` counterparts.

### Table of contents

1. [Dependencies](#dependencies)
2. [Usage](#usage)
  1. [Setup](#setup)
  2. [Simple example](#simple-example)
  3. [Type classes](#type-classes)
    1. [Lawless](#lawless)
    2. [Local](#local)
    3. [Zoned](#zoned)
    4. [Provider](#provider)
  4. [Instances](#instances)
    1. [JVM instances](#jvm-instances)
    2. [JS instances](#js-instances)
  5. [Syntax and Cats integration](#syntax-and-cats-integration)
3. [Motivation](#motivation)
4. [Modules](#modules)
5. [Known issues](#known-issues)

## Dependencies

DTC core depends on:
- [scala-js-java-time](https://github.com/scala-js/scala-js-java-time) to take advantage of `java.time._` API parts, that are already available for ScalaJS
- small [cats-kernel](https://github.com/typelevel/cats) module for `Order` type class.

## Usage

### Setup

#### Core
Add this line to your `build.sbt`.

```scala
libraryDependencies += "ru.pavkin" %%% "dtc-core" % "1.0.0"
```

#### MomentJS instances
If you want to use momentjs instances for ScalaJS runtime (see [JS instances](#js-instances)), also add `dtc-moment` module dependency to your scalajs subproject as well:

```scala
libraryDependencies += "ru.pavkin" %%% "dtc-moment" % "1.0.0"
```
This will add [momentjs](http://momentjs.com/) to your JS and [scala-js-momentjs](https://github.com/vpavkin/scala-js-momentjs) to your scalaJS dependencies.

#### Cats instances

Some additional cats type class instances for DTC type classes (like [Invariant](http://typelevel.org/cats/typeclasses/invariant.html)) are available via dtc-cats module:

```scala
libraryDependencies += "ru.pavkin" %%% "dtc-cats" % "1.0.0"
```

This will bring in [cats-core](https://github.com/typelevel/cats) dependency.


### Simple example

Let's create a simple polymorphic class that works with local datetime values.

Say we want a period entity that operates on local datetime values and knows both it's start and end:

```scala
import java.time.Duration // this is provided crossplatformly by scala-js-java-time

import dtc.Local
import dtc.syntax.local._ // syntax extensions for Local instances

case class Period[T: Local](start: T, end: T) {

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

Next, nothing stops us from creating a JS app as well:

```scala
import java.time.Duration

import dtc.js.JSDate // this is special wrapper around plain JS date, that provides basic FP guarantees, e.g. immutability
import dtc.instances.jsDate._ // implicit Local instance for JSDate
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


These examples demonstrate the core idea of the project.
Read further to check out the list of available type classes and instances.

---
### Type classes

*Disclaimer*: although following entities are called type classes, there are not "pure". 
For example, they can throw exceptions for invalid method parameters. This is intentional:

**Primary goal is to provide API that looks like `java.time._` as much as it's possible.**

DTC provides 4 type classes.

#### `Lawless`

`Lawless extends cats.kernel.Order` ([api](https://github.com/vpavkin/dtc/blob/master/core/shared/src/main/scala/dtc/Lawless.scala))

Base type class, that can be used for both local and zoned datetime instances.

All instances in DTC are extending `Lawless`
Most of the APIs are same for any datetime value, so with this typeclass you get:
  - all common methods and syntax except ones that are specific to local or zoned datetime values (e.g. constructors)
  - you can use both zoned and local datetime instances to fill in the type parameter (not simultaneously, of course)
  - almost no laws within the polymorphic code context :)

#### `Local`

`Local extends Lawless` ([api](https://github.com/vpavkin/dtc/blob/master/core/shared/src/main/scala/dtc/Local.scala))

Type class for values, that behave similarly to `java.time.LocalDateTime`. Instances hold local datetime laws.

#### `Zoned`

`Zoned extends Lawless` ([api](https://github.com/vpavkin/dtc/blob/master/core/shared/src/main/scala/dtc/Zoned.scala))

Type class for values, that behave similarly to `java.time.ZonedDateTime`. Instances hold zoned datetime laws.

#### `Provider`

[Api](https://github.com/vpavkin/dtc/blob/master/core/shared/src/main/scala/dtc/Zoned.scala)

Type class, that abstracts over the notion of "current" time. Provides API to get current date/time in a particular time zone.

In polymorphic context a `Provider` is **the only way** to get current time in DTC. This facilitates explicit DI of current time, which leads to better design. For example, it allows to work with artificial time, controlled from the outside (useful for tests).

---
### Instances

To make your polymorphic code work on a specific platform, you'll need to supply typeclass instances for concrete datetime types you use.

DTC provides instances for both JVM and ScalaJS.

#### JVM instances

For JVM everything is straightforward:

1. `java.time.LocalDateTime` has an instance of `Local`.
2. `java.time.ZonedDateTime` has an instance of `Zoned`.

To get the instances, just import respectively

```scala
import dtc.instances.localDateTime._
// or
import dtc.instances.zonedDateTime._
```

Also both `LocalDateTime` and `ZonedDateTime` have an implicit instance of real system time `Provider`, available in:
```scala
import dtc.instances.providers._
```

#### JS instances

First of all, DTC **does not provide** instances for **raw** js values (neither `Date` nor `moment`). 
They are mess to work with directly for two reasons:

1. they are mutable
2. they have totally different semantics, comparing to `java.time._`.

Instead, DTC provides simple wrappers that delegate to underlying values (or even enrich the available API).
These wrappers provide immutability guarantees and adapt the behaviour to follow `java.time._` semantics.

For ease of direct use, they reflect typeclass API as much as possible. 
Though, amount of actual direct use of them should be naturally limited, because, well... you can write polymorphic code instead!

##### `JSDate`

`JSDate` wraps native ECMA-Script Date and provides instance for `Local`.

Instance can be imported like this:
```scala
import dtc.instances.jsDate._
```

Javascript date has a very limited API which doesn't allow to handle time zones in a proper way. So there's no `Zoned` instance for it.

If you need a `Zoned` instance for your ScalaJS code, take a look at moment submodule, which is following next.

As on JVM, to get a real time `Provider[JSDate]`, add this to your imports:
```scala
import dtc.instances.providers._
```

##### `MomentLocalDateTime` and `MomentZonedDateTime`

These are based on popular [MomentJS](http://momentjs.com/) javascript library as well as [ScalaJS facade](https://github.com/vpavkin/scala-js-momentjs) for it.

To add them to your project, you'll need an explicit dependency on `dtc-moment` module:
```scala
libraryDependencies += "ru.pavkin" %%% "dtc-moment" % "1.0.0"
```

Both classes wrap `moment.Date` and, as you can guess:
- `MomentLocalDateTime` has a `Local` instance, and
- `MomentZonedDateTime` has a `Zoned` instance

You can get both instances in scope by adding:
```scala
import dtc.instances.moment._
```

`Provider` instances for real time can be obtained here:
```scala
import dtc.instances.moment.providers._
```
---
### Syntax and Cats integration

#### DTC syntax

When writing polymorphic code with DTC, it's very convenient to use syntax extensions.
They are similar to what Cats or ScalaZ provide for their type classes.

Just add following to your imports:
```scala
import dtc.syntax.local._  // for Local syntax
// or
import dtc.syntax.zoned._  // for Zoned syntax
```

If you need syntax for `Lawless` or for both local and zoned type classes in the same file,
just import all syntax at once:
```scala
import dtc.syntax.all._
```

#### `cats.kernel.Order` syntax.

Though, DTC provides basic API for datetime values comparison, it's more convenient and readable to use operators like `<`, `>=` and so on.
To pull this off, you will need syntax extensions for `cats.kernel.Order`, that is extended by all DTC type classes.

Unfortunately, kernel doesn't have syntax extensions.
So, to get this syntax, you'll need to add `dtc-cats` module or define an explicit `cats-core` dependency in your project:
```scala
libraryDependencies += "org.typelevel" %%% "cats-core" % "0.9.0"
```

After that just add regular cats import to get `Order` syntax for datetime values:
```scala
import cats.syntax.order._
```

## Motivation

See my [article](http://pavkin.ru/cross-platform-polymorphic-datetime-values-in-scala-with-type-classes/) for original motivation and implementation overview.

## Modules

DTC modules with published artifacts:

1. `dtc-core` - all type classes and instances for `java.time._` and `JSDate`
2. `dtc-moment` - momentjs instances (ScalaJS only)
3. `dtc-cats` - additional cats instances for dtc type classes, like [Invariant](http://typelevel.org/cats/typeclasses/invariant.html) (adds cats-core dependency).
4. `dtc-laws` - [discipline](https://github.com/typelevel/discipline) laws to test your own instances


## Known issues

#### MomentJS #3029
There's an open longstanding [bug](https://github.com/moment/moment/issues/3029) in MomentJS. 
In some rare cases it gives incorrect diffs for `monthsUntil` method.

As of current version of DTC, this bug leaks into momentjs instances as well.





