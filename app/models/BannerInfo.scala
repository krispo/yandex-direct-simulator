package models

/* method GetBanners (Live) -----  for postBannerReports ----------------------------------------------*/
/* output */
case class BannerInfo(
  val BannerID: Long,
  val Text: String,
  val Geo: String,
  val Phrases: List[BannerPhraseInfo])