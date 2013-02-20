package controllers

import play.api._
import play.api.mvc._

object Yandex extends Controller {

  def api = Action(parse.json) { implicit request =>
    val method = (request.body \ "method").as[String]
    
    method match {
      case "PingAPI" => {
       Ok 
      }
      case "GetCampaignsList" => {
        Ok
      }
      case "GetBanners" => {
        Ok
      }
      case "GetSummaryStat" => {
        Ok
      }
      case "CreateNewReport" => {
        Ok
      }
      case "GetReportList" => {
        Ok
      }
      case "DeleteReport" => {
        Ok
      }
      case "UpdatePrices" => {
        Ok
      }
    }
  }
}