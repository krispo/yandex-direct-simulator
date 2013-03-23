package jobs

import controllers._
import models._
import org.quartz._
import org.quartz.impl.StdSchedulerFactory

import dao.squerylorm.SquerylDao
import domain.PositionValue
import org.joda.time._

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
          .withIntervalInSeconds(5)
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

    val dao = new SquerylDao
    val time = new DateTime(jec.getFireTime())

    //generate NetAdvisedBids
    var c = dao.getCampaign("krisp0", 1).get

    val mu = PositionValue(0.01, 2.00, 2.50, 3.00, 0.20)
    val sigma = PositionValue(0.001, 0.1, 0.1, 0.1)

    c.bannerPhrases map { bp =>
      val nab = dao.generateNetAdvisedBids(bp, mu, sigma, time)
      dao.updatePrices(bp.id, nab.c, time)
    }

    //generate BannerPhrasePerformance
    c = dao.getCampaign("krisp0", 1).get
    c.bannerPhrases map { bp =>
      dao.generateBannerPhrasePerformance(bp, time)
    }

    //generate CampaignPerformance
    c = dao.getCampaign("krisp0", 1).get
    dao.generateCampaignPerformance(c, time)

    //generate Budget
    c = dao.getCampaign("krisp0", 1).get
    dao.generateBudget(c, time)

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