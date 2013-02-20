package models

import org.joda.time._

import play.libs.Scala
import java.util.Date

import play.api.libs.json._
import play.api.libs.functional.syntax._

/*----------- METHODS ------------*/

/* respond data for method GetCampaignsList */
case class ShortCampaignInfo(
  val CampaignID: Int = 0,
  val Login: String = "",
  val Name: String = "",
  val StartDate: DateTime = new DateTime,
  val Sum: Double = 0.0,
  val Rest: Double = 0.0,
  val Shows: Int = 0,
  val Clicks: Int = 0,
  val SumAvailableForTransfer: Option[Double] = Some(0.0),
  val Status: Option[String] = None,
  val StatusShow: Option[String] = None,
  val StatusArchive: Option[String] = None,
  val StatusActivating: Option[String] = None,
  val StatusModerate: Option[String] = None,
  val IsActive: Option[String] = None,
  val ManagerName: Option[String] = None,
  val AgencyName: Option[String] = None)

/* method GetSummaryStat -----  for postStats ----------------------------------------------*/
/* input T */
case class GetSummaryStatRequest(
  val CampaignIDS: List[Int],
  val StartDate: String, //Date
  val EndDate: String)

/* output List[T] */
case class StatItem(
  // other parameters are not useful yet  
  val SumSearch: Double = 0.0,
  val SumContext: Double = 0.0,
  val ShowsSearch: Int = 0,
  val ShowsContext: Int = 0,
  val ClicksSearch: Int = 0,
  val ClicksContext: Int = 0)

/* ------------------------  for postReports ----------------------------------------------*/
/* ----- method CreateNewReport ------------------------------*/
/* input T */
case class NewReportInfo(
  val CampaignID: Int,
  val StartDate: String, //Date
  val EndDate: String, //Date
  val GroupByColumns: List[String] = List("clBanner", "clPhrase")) //, "clPage", "clGeo", "clPositionType"))

/* output Report ID : Int,  {"data" : 123456} */

/* ----- method GetReportList --------------------------------*/
/* input T  - None.type */
/* output List[T] */

case class ReportInfo(
  val ReportID: Int,
  val Url: Option[String] = None,
  val StatusReport: String)

/* ----- method DeleteReport ---------------------------------*/
/* input T  - Report ID : Int */
/* output If success: {"data" : 1} */

/* method GetBanners (Live) -----  for postBannerReports ----------------------------------------------*/
/* input */
case class GetBannersInfo(
  val CampaignIDS: List[Int],
  //val FieldsNames: List[String] = List("BannerID", "Text", "Geo", "Phrases"),
  val GetPhrases: String = "WithPrices")

/* output */
case class BannerInfo(
  val BannerID: Long,
  val Text: String,
  val Geo: String,
  val Phrases: List[BannerPhraseInfo])

case class BannerPhraseInfo(
  val BannerID: Long = 0,
  val PhraseID: Long = 0,
  val CampaignID: Long = 0,
  val Phrase: String = "",
  val Min: Double = 0.0,
  val Max: Double = 0.0,
  val PremiumMin: Double = 0.0,
  val PremiumMax: Double = 0.0,
  val ContextPrice: Option[Double] = Some(0.0), //CPC on sites in the Yandex Advertising Network
  val AutoBroker: String = "", // Yes / No
  val Price: Double = 0.0, // Maximum CPC on Yandex search set for the phrase.
  val CurrentOnSearch: Option[Double] = Some(0.0) //The current CPC set by Autobroker
  )

/* method UpdatePrice -----------  use after getRecommendation ----------------------------------------------*/
/* input */
case class PhrasePriceInfo(
  val PhraseID: Int = 0,
  val BannerID: Int = 0,
  val CampaignID: Int = 0,
  val Price: Double = 0.0,
  val AutoBroker: Option[String] = Some("Yes"),
  val AutoBudgetPriority: Option[String] = Some("Medium"),
  val ContextPrice: Option[Double] = Some(0.0))

/* output */
// {"data" : 1} if successful 