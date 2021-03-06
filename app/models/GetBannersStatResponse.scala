package models

/**
 * GetBannersStat method
 */
case class GetBannersStatResponse(
  val CampaignID: Int,
  val StartDate: String, //Date
  val EndDate: String, //Date
  val Stat: List[BannersStatItem])