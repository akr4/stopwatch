/*
 * Copyright 2012 Akira Ueda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import sbt._
import sbt.Keys._

object MyBuild extends Build {

  val groupName = "stopwatch"

  def id(name: String) = "%s-%s" format(groupName, name)

  override val settings = super.settings :+ 
    (shellPrompt := { s => Project.extract(s).currentProject.id + "> " })

  val buildSettings = Defaults.defaultSettings ++ Seq(
    version := "1.1",
    organization := "net.physalis",
    crossScalaVersions := Seq("2.9.0", "2.9.0-1", "2.9.1"),
    scalaVersion := "2.9.1",
    scalacOptions ++= Seq("-Xcheckinit", "-encoding", "utf8"),
    publishTo <<= (version) { version: String =>
      val local = Path.userHome / "projects" / "akr4.github.com" / "mvn-repo"
      val path = local / (if (version.trim.endsWith("SNAPSHOT")) "snapshots" else "releases")
      Some(Resolver.file("Github Pages", path)(Patterns(true, Resolver.mavenStyleBasePattern)))
    },
    publishMavenStyle := true
  )

  val loggingDependencies = Seq(
    "org.slf4j" % "slf4j-api" % "1.6.2" withSources,
    "org.clapper" %% "grizzled-slf4j" % "0.6.6"
  )

  val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % "1.6.1" % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "latest.integration" % "test",
    "ch.qos.logback" % "logback-classic" % "0.9.25" % "test"
  )

  lazy val main = Project(groupName, file("."),
    settings = buildSettings ++ Seq(
      libraryDependencies := loggingDependencies ++ testDependencies,
      initialCommands in console := """
        import net.physalis.stopwatch._
      """.stripMargin
    )
  )
}

