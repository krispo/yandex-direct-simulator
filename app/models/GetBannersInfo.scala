package models

/* method GetBanners (Live) -----  for postBannerReports ----------------------------------------------*/
/* input */
case class GetBannersInfo(
  val CampaignIDS: List[Int],
  //val FieldsNames: List[String] = List("BannerID", "Text", "Geo", "Phrases"),
  val GetPhrases: String = "WithPrices")
