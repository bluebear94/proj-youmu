name := "thsch"

version := "0.1"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
	"org.scalatest" %% "scalatest" % "2.2.1",
	"com.sksamuel.scrimage" %% 
"scrimage-core" % "1.4.2"
)

unmanagedBase := baseDirectory.value / "lib"

initialCommands in console := """println("Remember, case classes have the .copy method!")"""

cleanupCommands in console := """println("Awww, returning to Java so soon?")"""

autoCompilerPlugins := true

addCompilerPlugin("org.scala-lang.plugins" % "scala-continuations-plugin_2.11.2" % "1.0.2")

libraryDependencies += "org.scala-lang.plugins" % "scala-continuations-library_2.11" % "1.0.2"

scalacOptions ++= Seq("-P:continuations:enable", "-Djava.library.path=lib/native", "-feature", "-language:implicitConversions")
