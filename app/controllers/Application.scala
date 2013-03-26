package controllers

import jobs._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import dao.squerylorm.SquerylDao
import org.joda.time._

case class Prior(
  val min: Double,
  val max: Double,
  val pMin: Double,
  val pMax: Double)

case class DBParams(
  val nCampaigns: Integer,
  val nfBanners: Integer,
  val prior: Prior,
  val nPhrasesInBanner: Integer)

object Application extends Controller {

  def index = Action {
    Ok(views.html.index(Scheduler.isStarted & !Scheduler.isInStandbyMode))
  }

  def charts = Action {
    val dao = new SquerylDao

    val c = dao.getCampaign("krisp0", 1)
    val b = c.get.budgetHistory map (bh => (new DateTime(bh.date).getMillis(), bh.budget))
       
    val nab = c.get.bannerPhrases map (bp=>bp.netAdvisedBidsHistory)
    Ok(views.html.charts(c))//b.reverse.tail)) //exclude first case
  }

  def startJobs = Action {
    Scheduler.start
    Ok(views.html.index(Scheduler.isStarted & !Scheduler.isInStandbyMode))
  }

  def stopJobs = Action {
    Scheduler.stop
    Ok(views.html.index(Scheduler.isStarted & !Scheduler.isInStandbyMode))
  }

  /**
   * clear database function
   * is used with GET request from web client
   */
  def clearDB = Action {
    if ((new dao.squerylorm.SquerylDao).clearDB) {
      println("!!! DB is CLEAR !!!")
      Ok
    } else {
      println("??? DB is NOT clear... ???")
      BadRequest
    }
  }

}