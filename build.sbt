import ReleaseTransformations._
import sbtcrossproject.CrossPlugin.autoImport.crossProject

lazy val buildSettings = Seq(
  organization := "ru.pavkin",
  scalaVersion := "2.12.10",
  crossScalaVersions := Seq("2.12.10", "2.13.1")
)

lazy val compilerOptions = Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-unchecked",
  "-Xfatal-warnings",
  "-Ywarn-dead-code"
)


lazy val catsVersion = "2.1.1"
lazy val simulacrumVersion = "1.0.0"
lazy val scalaJSJavaTimeVersion = "0.2.6"
lazy val disciplineVersion = "1.0.2"
lazy val disciplineScalatestVersion = "1.0.1"
lazy val scalaCheckDateTimeVersion = "0.3.2"
lazy val scalaCheckVersion = "1.14.3"
lazy val scalaTestVersion = "3.1.1"
lazy val scalaCollectionCompatVersion = "2.1.4"

lazy val momentFacadeVersion = "0.10.2"

lazy val macroAnnotationOption = Seq(
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 13)) =>
        Seq("-Ymacro-annotations")
      case _ => Nil
    }
  }
)

lazy val baseSettings = macroAnnotationOption ++ Seq(
  scalacOptions ++= compilerOptions ++ Seq(
    "-Ywarn-unused:imports"
  ),
  testOptions in Test += Tests.Argument("-oF"),
  scalacOptions in(Compile, console) := compilerOptions,
  scalacOptions in(Compile, test) := compilerOptions,
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases")
  ),

  libraryDependencies ++= {
    List("org.typelevel" %%% "simulacrum" % simulacrumVersion) ++
      (CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 12)) =>
          compilerPlugin("org.scalamacros" %% "paradise" % "2.1.1" cross CrossVersion.full) :: Nil
        case _ => Nil
      })
  }
)

lazy val allSettings = buildSettings ++ baseSettings ++ publishSettings

lazy val dtc = project.in(file("."))
  .settings(name := "dtc")
  .settings(allSettings: _*)
  .settings(docSettings: _*)
  .settings(noPublishSettings: _*)
  .enablePlugins(ScalaUnidocPlugin)
  .enablePlugins(GhpagesPlugin)
  .aggregate(coreJVM, coreJS, moment, lawsJVM, lawsJS, catsJVM, catsJS, examplesJVM, examplesJS, testsJS, testsJVM)
  .dependsOn(coreJVM, coreJS, moment, lawsJVM, lawsJS, catsJVM, catsJS, examplesJVM, examplesJS, testsJS, testsJVM)

lazy val core = (crossProject(JSPlatform, JVMPlatform) in file("core"))
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
  .enablePlugins(ScalaJSBundlerPlugin)
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

lazy val cats = (crossProject(JSPlatform, JVMPlatform) in file("cats"))
  .settings(
    description := "DTC cats",
    moduleName := "dtc-cats",
    name := "cats"
  )
  .settings(allSettings: _*)
  .settings(
    libraryDependencies += "org.typelevel" %%% "cats-core" % catsVersion
  )
  .jsConfigure(_.enablePlugins(ScalaJSBundlerPlugin))
  .dependsOn(core)

lazy val catsJVM = cats.jvm
lazy val catsJS = cats.js

lazy val laws = (crossProject(JSPlatform, JVMPlatform) in file("laws"))
  .settings(
    description := "DTC laws",
    moduleName := "dtc-laws",
    name := "laws"
  )
  .settings(allSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "org.typelevel" %%% "discipline-core" % disciplineVersion,
    "org.typelevel" %%% "discipline-scalatest" % disciplineScalatestVersion,
    "org.typelevel" %%% "cats-kernel" % catsVersion,
    "org.typelevel" %%% "cats-core" % catsVersion,
    "org.typelevel" %%% "cats-kernel-laws" % catsVersion
  ))
  .settings(
    coverageExcludedPackages := "dtc\\.laws\\..*"
  )
  .jsConfigure(_.enablePlugins(ScalaJSBundlerPlugin))
  .dependsOn(core)

lazy val lawsJVM = laws.jvm
lazy val lawsJS = laws.js

lazy val examples = (crossProject(JSPlatform, JVMPlatform) in file("examples"))
  .settings(
    description := "DTC examples",
    moduleName := "dtc-examples",
    name := "examples"
  )
  .settings(allSettings: _*)
  .settings(noPublishSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats-core" % catsVersion
  ))
  .settings(
    coverageExcludedPackages := "dtc\\.examples\\..*"
  )
  .jsConfigure(_.enablePlugins(ScalaJSBundlerPlugin))
  .dependsOn(core)

lazy val examplesJVM = examples.jvm
lazy val examplesJS = examples.js

lazy val tests = (crossProject(JSPlatform, JVMPlatform) in file("tests"))
  .settings(
    description := "DTC tests",
    moduleName := "dtc-tests",
    name := "tests"
  )
  .settings(allSettings: _*)
  .settings(noPublishSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "org.typelevel" %%% "discipline-core" % disciplineVersion % "test",
    "org.scalacheck" %%% "scalacheck" % scalaCheckVersion % "test",
    "org.scalatest" %%% "scalatest" % scalaTestVersion % "test",
    "com.47deg" %% "scalacheck-toolbox-datetime" % scalaCheckDateTimeVersion % "test",
    "org.scala-lang.modules" %% "scala-collection-compat" % scalaCollectionCompatVersion % "test"
  ))
  .settings(
    coverageExcludedPackages := "dtc\\.tests\\..*"
  )
  .jsConfigure(_.enablePlugins(ScalaJSBundlerPlugin))
  .dependsOn(core, laws)
  .jsConfigure(_.dependsOn(moment))

lazy val testsJVM = tests.jvm
lazy val testsJS = tests.js

lazy val noDocProjects: Seq[ProjectReference] =
  Seq(dtc, coreJS, catsJS, lawsJVM, lawsJS, testsJVM, testsJS, examplesJVM, examplesJS)

lazy val docSettings = Seq(
  siteSubdirName in ScalaUnidoc := "api",
  addMappingsToSiteDir(mappings in(ScalaUnidoc, packageDoc), siteSubdirName in ScalaUnidoc),
  scalacOptions in(ScalaUnidoc, unidoc) ++= Seq(
    "-Ymacro-expand:none",
    "-groups",
    "-implicits",
    "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
    "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath
  ),
  git.remoteRepo := "git@github.com:vpavkin/dtc.git",
  unidocProjectFilter in(ScalaUnidoc, unidoc) := (inAnyProject -- inProjects(noDocProjects: _*))
)

lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
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
    ReleaseStep(action = releaseStepCommand("sonatypeReleaseAll")),
    pushChanges
  )
)

addCommandAlias("validate", ";compile;testsJVM/test;testsJS/test")
