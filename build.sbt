import sbtunidoc.Plugin.UnidocKeys._
import ReleaseTransformations._

lazy val buildSettings = Seq(
  organization := "ru.pavkin",
  scalaVersion := "2.12.2",
  crossScalaVersions := Seq("2.11.11", "2.12.2")
)

lazy val compilerOptions = Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-unchecked",
  "-Xfatal-warnings",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Xfuture"
)


lazy val catsVersion = "0.9.0"
lazy val simulacrumVersion = "0.10.0"
lazy val scalaJSJavaTimeVersion = "0.2.0"
lazy val disciplineVersion = "0.7.3"
lazy val scalaCheckDateTimeVersion = "0.2.1"
lazy val scalaCheckVersion = "1.13.4"
lazy val scalaTestVersion = "3.0.1"

lazy val momentFacadeVersion = "0.8.0"

lazy val baseSettings = Seq(
  scalacOptions ++= compilerOptions ++ Seq(
    "-Ywarn-unused-import"
  ),
  testOptions in Test += Tests.Argument("-oF"),
  scalacOptions in(Compile, console) := compilerOptions,
  scalacOptions in(Compile, test) := compilerOptions,
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  ),
  libraryDependencies ++= Seq(
    "com.github.mpilquist" %%% "simulacrum" % simulacrumVersion,
    compilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.full)
  )
)

lazy val allSettings = buildSettings ++ baseSettings ++ publishSettings

lazy val dtc = project.in(file("."))
  .settings(name := "dtc")
  .settings(allSettings: _*)
  .settings(docSettings: _*)
  .settings(noPublishSettings: _*)
  .aggregate(coreJVM, coreJS, moment, lawsJVM, lawsJS, catsJVM, catsJS, examplesJVM, examplesJS, testsJS, testsJVM)
  .dependsOn(coreJVM, coreJS, moment, lawsJVM, lawsJS, catsJVM, catsJS, examplesJVM, examplesJS, testsJS, testsJVM)

lazy val core = (crossProject in file("core"))
  .settings(
    description := "DTC core",
    moduleName := "dtc-core",
    name := "core"
  )
  .settings(allSettings: _*)
  .settings(
    libraryDependencies += "org.typelevel" %%% "cats-kernel" % catsVersion
  )
  .jsSettings(
    libraryDependencies += "org.scala-js" %%% "scalajs-java-time" % scalaJSJavaTimeVersion
  )

lazy val coreJVM = core.jvm
lazy val coreJS = core.js

lazy val moment = project.in(file("moment"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    description := "DTC moment",
    moduleName := "dtc-moment",
    name := "moment"
  )
  .settings(allSettings: _*)
  .settings(
    libraryDependencies += "ru.pavkin" %%% "scala-js-momentjs" % momentFacadeVersion
  )
  .dependsOn(coreJS)

lazy val cats = (crossProject in file("cats"))
  .settings(
    description := "DTC cats",
    moduleName := "dtc-cats",
    name := "cats"
  )
  .settings(allSettings: _*)
  .settings(
    libraryDependencies += "org.typelevel" %%% "cats-core" % catsVersion
  )
  .dependsOn(core)

lazy val catsJVM = cats.jvm
lazy val catsJS = cats.js

lazy val laws = (crossProject in file("laws"))
  .settings(
    description := "DTC laws",
    moduleName := "dtc-laws",
    name := "laws"
  )
  .settings(allSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "org.typelevel" %%% "discipline" % disciplineVersion,
    "org.typelevel" %%% "cats-kernel" % catsVersion,
    "org.typelevel" %%% "cats" % catsVersion,
    "org.typelevel" %%% "cats-kernel-laws" % catsVersion
  ))
  .settings(
    coverageExcludedPackages := "dtc\\.laws\\..*"
  )
  .dependsOn(core)

lazy val lawsJVM = laws.jvm
lazy val lawsJS = laws.js

lazy val examples = (crossProject in file("examples"))
  .settings(
    description := "DTC examples",
    moduleName := "dtc-examples",
    name := "examples"
  )
  .settings(allSettings: _*)
  .settings(noPublishSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats" % catsVersion
  ))
  .settings(
    coverageExcludedPackages := "dtc\\.examples\\..*"
  )
  .dependsOn(core)

lazy val examplesJVM = examples.jvm
lazy val examplesJS = examples.js

lazy val tests = (crossProject in file("tests"))
  .settings(
    description := "DTC tests",
    moduleName := "dtc-tests",
    name := "tests"
  )
  .settings(allSettings: _*)
  .settings(noPublishSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "org.typelevel" %%% "discipline" % disciplineVersion % "test",
    "org.scalacheck" %%% "scalacheck" % scalaCheckVersion % "test",
    "org.scalatest" %%% "scalatest" % scalaTestVersion % "test",
    "com.fortysevendeg" %% "scalacheck-toolbox-datetime" % scalaCheckDateTimeVersion % "test"
  ))
  .settings(
    coverageExcludedPackages := "dtc\\.tests\\..*"
  )
  .jsSettings(scalaJSUseRhino in Global := true)
  .dependsOn(core, laws)
  .jsConfigure(_.dependsOn(moment))

lazy val testsJVM = tests.jvm
lazy val testsJS = tests.js

lazy val noDocProjects: Seq[ProjectReference] =
  Seq(dtc, coreJS, catsJS, lawsJVM, lawsJS, testsJVM, testsJS, examplesJVM, examplesJS)

lazy val docSettings = site.settings ++ ghpages.settings ++ unidocSettings ++ Seq(
  site.addMappingsToSiteDir(mappings in(ScalaUnidoc, packageDoc), "api"),
  scalacOptions in(ScalaUnidoc, unidoc) ++= Seq(
    "-groups",
    "-implicits",
    "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
    "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath
  ),
  git.remoteRepo := "git@github.com:vpavkin/dtc.git",
  unidocProjectFilter in(ScalaUnidoc, unidoc) := (inAnyProject -- inProjects(noDocProjects: _*))
)

lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

lazy val publishSettings = Seq(
  releaseIgnoreUntrackedFiles := true,
  releaseCrossBuild := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  homepage := Some(url("https://github.com/vpavkin/dtc")),
  licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  autoAPIMappings := true,
  apiURL := Some(url("https://vpavkin.github.io/dtc/api/")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/vpavkin/dtc"),
      "scm:git:git@github.com:vpavkin/dtc.git"
    )
  ),
  pomExtra :=
    <developers>
      <developer>
        <id>vpavkin</id>
        <name>Vladimir Pavkin</name>
        <url>http://pavkin.ru</url>
      </developer>
    </developers>
)

lazy val sharedReleaseProcess = Seq(
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    publishArtifacts,
    setNextVersion,
    commitNextVersion,
    ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
    pushChanges
  )
)

addCommandAlias("validate", ";compile;testsJVM/test;testsJS/test")
