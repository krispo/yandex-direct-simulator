package controllers

import jobs._

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

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

  def startJobs(username: String) = Action {
    Scheduler.start
    Ok(views.html.index(Scheduler.isStarted & !Scheduler.isInStandbyMode))
  }

  def stopJobs(username: String) = Action {
    Scheduler.stop
    Ok(views.html.index(Scheduler.isStarted & !Scheduler.isInStandbyMode))
  }
}