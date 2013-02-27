package jobs

import scala.concurrent.duration._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.concurrent.Akka
import controllers._
import models._
import java.util.Date
import java.util.concurrent.TimeUnit

import play.api.Play.current //or use (implicit app: play.api.Application)

import org.joda.time.DateTime

object Scheduler {

  def start {
    Akka.system.scheduler.schedule(0 seconds, 1 minutes) {
      println("!!! START Job ============================================================================ !!!")
      //executeBlock
      println("!!! END Job ============================================================================== !!!")
    }
  }

  def shutdown {
    println("!!! SCHEDULER has STOPPED !!!")
    Akka.system.shutdown()
  }
}