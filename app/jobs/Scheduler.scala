package jobs

import controllers._
import models._
import org.quartz._
import org.quartz.impl.StdSchedulerFactory

object Scheduler {

  val scheduler = StdSchedulerFactory.getDefaultScheduler()

  val jKey = JobKey.jobKey("job")
  val tKey = TriggerKey.triggerKey("trigger")

  def start = {
    scheduler.clear() //remove all jobs and triggers
    scheduler.start()

    println("================== SHEDULER start ==================")

    // define the job and tie it to our executeBlock class
    val job = JobBuilder.newJob(classOf[executeBlock]).withIdentity(jKey).build()

    // Trigger the job to run now, and then repeat every 10 seconds
    val trigger = TriggerBuilder.newTrigger()
      .withIdentity(tKey)
      .startNow()
      .withSchedule(
        SimpleScheduleBuilder.simpleSchedule()
          .withIntervalInSeconds(1)
          .repeatForever())
      .build()

    // Tell quartz to schedule the job using our trigger
    println(scheduler.scheduleJob(job, trigger))

  }

  def stop = {
    scheduler.standby()
    println("================== SHEDULER stop ==================")
  }

  def shutdown = {
    scheduler.shutdown()
    println("================== SHEDULER shutdown ==================")
  }

  def isStarted: Boolean = scheduler.isStarted()
  def isInStandbyMode: Boolean = scheduler.isInStandbyMode()
}

class executeBlock extends Job {
  def execute(jec: JobExecutionContext) {
    println("------------------ START Job ------------------")
    println("------------------ END Job ------------------")
  }

} 


//import scala.concurrent.duration._
//import play.api.libs.concurrent.Execution.Implicits._
//import play.api.libs.concurrent.Akka
//import java.util.Date
//import java.util.concurrent.TimeUnit

//import play.api.Play.current //or use (implicit app: play.api.Application)
 /*def start: akka.actor.Cancellable = { //(implicit app: play.api.Application) {
    val aks = Akka.system
    val can = aks.scheduler.schedule(5 seconds, 5 seconds) {
      println("!!! START Job ============================================================================ !!!")
      //executeBlock
      println("!!! END Job ============================================================================== !!!")
    }
    can
  }

  def shutdown {
    println("!!! SCHEDULER has STOPPED !!!")
    Akka.system.shutdown()
  }*/

//if (!scheduler.checkExists(jKey) & !scheduler.checkExists(tKey))