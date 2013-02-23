package controllers

import models._
import json_api.Convert._

import play.api._
import play.api.mvc._

import play.api.libs.json._

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

    method match {//+
      case "PingAPI" => Ok(Response(JsNumber(1))) as JSON

      case "GetCampaignsList" => {//+
        Ok(Response(toJson[List[ShortCampaignInfo]](ShortCampaignInfo.get(login, token))))
      }

      case "GetBanners" => {//+
        fromJson[GetBannersInfo](request.body \ "param") map { s =>
          Ok(Response(toJson[List[BannerInfo]](BannerInfo.get(login, token, s))))
        } getOrElse BadRequest
      }

      case "GetSummaryStat" => {//+
        fromJson[GetSummaryStatRequest](request.body \ "param") map { s =>
          Ok(Response(toJson[List[StatItem]](StatItem.get(login, token, s))))
        } getOrElse BadRequest
      }

      case "CreateNewReport" => {
        fromJson[NewReportInfo](request.body \ "param") map { s =>
          Ok(Response(JsNumber(s.generate)))
        } getOrElse BadRequest
      }

      case "GetReportList" => {
        Ok(Response(toJson[ReportInfo](ReportInfo.get(login, token))))
      }

      case "DeleteReport" => {
        (request.body \ "param").asOpt[Int] map { s =>
          //TODO
          Ok
        } getOrElse BadRequest
      }

      case "UpdatePrices" => {//+
        fromJson[List[PhrasePriceInfo]](request.body \ "param") map { s =>
          if (!UpdatePrice.update(login, token, s)) Ok else BadRequest
        } getOrElse BadRequest
      }

    }
  }
}