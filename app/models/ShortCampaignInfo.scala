package models

import org.joda.time._

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