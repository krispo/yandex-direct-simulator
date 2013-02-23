package models

import dao.squerylorm.SquerylDao

/* method GetBanners (Live) -----  for postBannerReports ----------------------------------------------*/
/* output */
case class BannerInfo(
  val BannerID: Long,
  val Text: String,
  val Geo: String,
  val Phrases: List[BannerPhraseInfo])

object BannerInfo {
  //retrieve from DB
  def get(login: String, token: String, gbi: GetBannersInfo): List[BannerInfo] = {
    val dao = new SquerylDao
    for {
      c <- gbi.CampaignIDS map (dao.getCampaign(_));
      b <- c.banners
    } yield {
      BannerInfo(
        BannerID = b.id,
        Text = b.text,
        Geo = "",
        Phrases = b.bannerPhrases map { bp =>
          val actualBid = bp.actualBidHistory.head
          val netAdvBid = bp.netAdvisedBidsHistory.head
          BannerPhraseInfo(
            BannerID = b.id,
            PhraseID = bp.id,
            CampaignID = c.id,
            Phrase = bp.phrase.get.phrase,
            Min = netAdvBid.a,
            Max = netAdvBid.b,
            PremiumMin = netAdvBid.c,
            PremiumMax = netAdvBid.d,
            Price = actualBid.elem)
        } toList)
    }
  }
}  