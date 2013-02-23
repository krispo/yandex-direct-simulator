import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "yandex_direct"
  val appVersion = "1.0-SNAPSHOT"

  val squeryl_orm = "org.squeryl" %% "squeryl" % "0.9.5-6"
  val postgresDriver = "postgresql" % "postgresql" % "9.1-901-1.jdbc4"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    squeryl_orm,
    postgresDriver)

  val main = play.Project(appName, appVersion, appDependencies).settings( // Add your own project settings here      
  )

}
