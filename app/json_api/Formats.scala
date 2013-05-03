package json_api

import models._

import play.api.libs.json._

/************* JSON WRITEs and READs for creating REQUESTs and handling RESULTs ************/
/************* or just define JSON FORMATs 									  ************/

object Reads { //-------------------------- fromJson ---------------------------------
  import play.api.libs.json.Reads._

  implicit lazy val getBannersInfo = Json.reads[GetBannersInfo]
  implicit lazy val getSummaryStatRequest = Json.reads[GetSummaryStatRequest]
  implicit lazy val newReportInfo = Json.reads[NewReportInfo]
  implicit lazy val phrasePriceInfo = Json.reads[PhrasePriceInfo]
}

object Writes { //---------------------- toJson -----------------------------------
  import play.api.libs.json.Writes._

  implicit lazy val shortCampaignInfo = Json.writes[ShortCampaignInfo]
  implicit lazy val bannerPhraseInfo = Json.writes[BannerPhraseInfo]
  implicit lazy val bannerInfo = Json.writes[BannerInfo]
  implicit lazy val statItem = Json.writes[StatItem]
  implicit lazy val reportInfo = Json.writes[ReportInfo]

  implicit lazy val bannersStatItem = Json.writes[BannersStatItem]
  implicit lazy val getBannersStatResponse = Json.writes[GetBannersStatResponse]

  implicit lazy val clientInfo = Json.writes[ClientInfo]
}
