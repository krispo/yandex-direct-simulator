package json_api

import models._

import play.api.libs.json._

object Convert {

  val typeList = Map(
    "ShortCampaignInfo" -> "models.ShortCampaignInfo",
    "List[ShortCampaignInfo]" -> "scala.collection.immutable.List[models.ShortCampaignInfo]",

    "BannerInfo" -> "models.BannerInfo",
    "List[BannerInfo]" -> "scala.collection.immutable.List[models.BannerInfo]",

    "StatItem" -> "models.StatItem",
    "List[StatItem]" -> "scala.collection.immutable.List[models.StatItem]",

    "ReportInfo" -> "models.ReportInfo",
    "List[ReportInfo]" -> "scala.collection.immutable.List[models.ReportInfo]",

    "GetBannersInfo" -> "models.GetBannersInfo",

    "GetSummaryStatRequest" -> "models.GetSummaryStatRequest",

    "NewReportInfo" -> "models.NewReportInfo",

    "PhrasePriceInfo" -> "models.PhrasePriceInfo",
    "List[PhrasePriceInfo]" -> "scala.collection.immutable.List[models.PhrasePriceInfo]",

    "User" -> "models.User",

    "Campaign" -> "models.Campaign",
    "List[Campaign]" -> "scala.collection.immutable.List[models.Campaign]",

    "Performance" -> "models.Performance",

    "GetBannersStatResponse" -> "models.GetBannersStatResponse")

  def fromJson[T](data: JsValue)(implicit mf: Manifest[T]): Option[T] = {
    import Reads._
    typeList.filter(_._2.equals(mf.toString)).headOption map {
      _._1 match {

        case "GetBannersInfo" =>
          Json.fromJson[GetBannersInfo](data) map (s => Some(s.asInstanceOf[T])) recoverTotal (e => None)

        case "GetSummaryStatRequest" =>
          Json.fromJson[GetSummaryStatRequest](data) map (s => Some(s.asInstanceOf[T])) recoverTotal (e => None)

        case "NewReportInfo" =>
          Json.fromJson[NewReportInfo](data) map (s => Some(s.asInstanceOf[T])) recoverTotal (e => None)

        case "PhrasePriceInfo" =>
          Json.fromJson[PhrasePriceInfo](data) map (s => Some(s.asInstanceOf[T])) recoverTotal (e => None)
        case "List[PhrasePriceInfo]" =>
          Json.fromJson[List[PhrasePriceInfo]](data) map (s => Some(s.asInstanceOf[T])) recoverTotal (e => None)
      }

    } getOrElse (None)
  }

  def toJson[T](data: T)(implicit mf: Manifest[T]): JsValue = {
    import Writes._
    typeList.filter(_._2.equals(mf.toString)).headOption map {
      _._1 match {

        case "ShortCampaignInfo" =>
          Json.toJson[ShortCampaignInfo](data.asInstanceOf[ShortCampaignInfo])
        case "List[ShortCampaignInfo]" =>
          Json.toJson[List[ShortCampaignInfo]](data.asInstanceOf[List[ShortCampaignInfo]])

        case "BannerInfo" =>
          Json.toJson[BannerInfo](data.asInstanceOf[BannerInfo])
        case "List[BannerInfo]" =>
          Json.toJson[List[BannerInfo]](data.asInstanceOf[List[BannerInfo]])

        case "StatItem" =>
          Json.toJson[StatItem](data.asInstanceOf[StatItem])
        case "List[StatItem]" =>
          Json.toJson[List[StatItem]](data.asInstanceOf[List[StatItem]])

        case "ReportInfo" =>
          Json.toJson[ReportInfo](data.asInstanceOf[ReportInfo])
        case "List[ReportInfo]" =>
          Json.toJson[List[ReportInfo]](data.asInstanceOf[List[ReportInfo]])

        case "GetBannersStatResponse" => //BannerPhrases Performance during the day (alternative to XMLreport)
          Json.toJson[GetBannersStatResponse](data.asInstanceOf[GetBannersStatResponse])
      }
    } getOrElse (JsNull)
  }
}