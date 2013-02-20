package controllers

import models._
import json_api.Convert._

import play.api._
import play.api.mvc._

import play.api.libs.json._

object Yandex extends Controller {

  def api = Action(parse.json) { implicit request =>
    val method = (request.body \ "method").as[String]

    method match {
      case "PingAPI" => Ok(Response(JsNumber(1))) as JSON

      case "GetCampaignsList" => {
        Ok
      }

      case "GetBanners" => {
        fromJson[GetBannersInfo](request.body \ "param") map { s =>
          Ok
        } getOrElse BadRequest
      }

      case "GetSummaryStat" => {
        fromJson[GetSummaryStatRequest](request.body \ "param") map { s =>
          Ok
        } getOrElse BadRequest
      }

      case "CreateNewReport" => {
        fromJson[NewReportInfo](request.body \ "param") map { s =>
          Ok
        } getOrElse BadRequest
      }

      case "GetReportList" => {
        Ok
      }

      case "DeleteReport" => {
        (request.body \ "param").asOpt[Int] map { s =>
          Ok
        } getOrElse BadRequest
      }

      case "UpdatePrices" => {
        fromJson[PhrasePriceInfo](request.body \ "param") map { s =>
          Ok
        } getOrElse BadRequest
      }

    }
  }
}