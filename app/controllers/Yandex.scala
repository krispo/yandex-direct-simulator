package controllers

import models._
import json_api.Convert._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import dao.squerylorm.SquerylDao

object Yandex extends Controller {

  def api = Action(parse.json) { implicit request =>
    /* input structure:
        "login",
        "token",
        "application_id",
        "locale",
        "method",
        "param"
    */
    val login = (request.body \ "login").as[String]
    val token = (request.body \ "token").as[String]
    val method = (request.body \ "method").as[String]
    val param = request.body \ "param"

    val api = new API(login)

    method match { //+
      case "PingAPI" => Ok(wrap(JsNumber(1))) as JSON

      case "GetCampaignsList" => { //++ 
        Ok(wrap(toJson[List[ShortCampaignInfo]](api.getCampaignsList))) as JSON
      }

      case "GetBanners" => { //++     ActualBids and NetAdvisedBids
        fromJson[GetBannersInfo](param) map { s =>
          Ok(wrap(toJson[List[BannerInfo]](api.getBanners(s)))) as JSON
        } getOrElse BadRequest
      }

      case "GetSummaryStat" => { //++    CampaignPerformance
        fromJson[GetSummaryStatRequest](param) map { s =>
          Ok(wrap(toJson[List[StatItem]](api.getSummaryStat(s)))) as JSON
        } getOrElse BadRequest
      }

      case "CreateNewReport" => { //++     BannerPhrasePerformance
        fromJson[NewReportInfo](param) map { s =>
          Ok(wrap(JsNumber(api.createNewReport(s)))) as JSON
        } getOrElse BadRequest
      }

      case "GetReportList" => { //++  
        Ok(wrap(toJson[List[ReportInfo]](api.getReportList))) as JSON
      }

      case "DeleteReport" => { //++  
        param.asOpt[Int] map { s =>
          Ok(wrap(JsNumber(api.deleteReport(s)))) as JSON
        } getOrElse BadRequest
      }

      case "UpdatePrices" => { //++     update ActualBids
        fromJson[List[PhrasePriceInfo]](param) map { s =>
          if (!api.updatePrices(s))
            Ok(wrap(JsNumber(1))) as JSON
          else
            BadRequest
        } getOrElse BadRequest
      }

    }
  }

  def report(reportId: Long) = Action {
    Ok(API("").getXml(reportId)) as XML
  }

  def wrap(data: JsValue): JsValue = Json.toJson(Json.obj(("data" -> data)))
}