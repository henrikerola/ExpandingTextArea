import org.vaadin.sbt.VaadinPlugin._
import sbt.Keys._
import sbt.ScalaVersion

name := "ExpandingTextArea"

version in ThisBuild := "1.1.0"

organization in ThisBuild := "org.vaadin.hene"

crossPaths in ThisBuild := false

autoScalaLibrary in ThisBuild := false

javacOptions in ThisBuild ++= Seq("-source", "1.6", "-target", "1.6")

lazy val root = project.in(file(".")).aggregate(addon, demo)

lazy val addon = project.settings(vaadinAddOnSettings :_*).settings(
  name := "ExpandingTextArea",
  libraryDependencies := Dependencies.addonDeps,
  // Javadoc generation causes problems so disabling it for now
  mappings in packageVaadinDirectoryZip <<= (packageSrc in Compile) map {
    (src) => Seq((src, src.name))
  },
  sources in doc in Compile := List()
)

lazy val demo = project.enablePlugins(JettyPlugin).settings(vaadinWebSettings :_*).settings(
  name := "expandingtextarea-demo",
  artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) => "ExpandingTextArea." + artifact.extension },
  libraryDependencies ++= Dependencies.demoDeps,
  javaOptions in compileVaadinWidgetsets := Seq("-Xss8M", "-Xmx512M", "-XX:MaxPermSize=512M"),
  vaadinOptions in compileVaadinWidgetsets := Seq("-strict", "-draftCompile"),
  skip in compileVaadinWidgetsets in resourceGenerators := true,
  javaOptions in vaadinDevMode ++= Seq("-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"),
  // JavaDoc generation causes problems
  sources in doc in Compile := List(),
  webappWebInfClasses := true
).dependsOn(addon)
