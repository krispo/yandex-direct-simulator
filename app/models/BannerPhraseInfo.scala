package models

/* method GetBanners (Live) -----  for postBannerReports ----------------------------------------------*/
/* output */
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