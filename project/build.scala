import sbt._
import sbt.Keys._

object MyBuild extends Build {

  val groupName = "stopwatch"

  def id(name: String) = "%s-%s" format(groupName, name)

  override val settings = super.settings :+ 
    (shellPrompt := { s => Project.extract(s).currentProject.id + "> " })

  val buildSettings = Defaults.defaultSettings ++ Seq(
    version := "0.1",
    organization := "net.physalis",
    crossScalaVersions := Seq("2.9.0", "2.9.0-1", "2.9.1"),
    scalaVersion := "2.9.1",
    scalacOptions ++= Seq("-Xcheckinit", "-encoding", "utf8")
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

