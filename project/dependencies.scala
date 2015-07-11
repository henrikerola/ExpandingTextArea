import sbt._
import sbt.Keys._

object Dependencies {

  val vaadinVersion = "7.4.0.beta2"

  val vaadinServer = "com.vaadin" % "vaadin-server" % vaadinVersion
  val vaadinClient = "com.vaadin" % "vaadin-client" % vaadinVersion
  val vaadinClientCompiler = "com.vaadin" % "vaadin-client-compiler" % vaadinVersion
  val vaadinThemes = "com.vaadin" % "vaadin-themes" % vaadinVersion

  val servletApi = "javax.servlet" % "servlet-api" % "2.5"

  val addonDeps = Seq(
    vaadinServer,
    vaadinClient
  )

  val demoDeps = Seq(
    //servletApi % "provided",
    vaadinClientCompiler % "provided",
    vaadinThemes
  )

}
