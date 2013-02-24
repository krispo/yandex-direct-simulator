package models

import dao.squerylorm._

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

object UpdatePrice {
  def update(login: String, token: String, ppil: List[PhrasePriceInfo]): Boolean = {
    val dao = new SquerylDao
    val cs = dao.getCampaigns(login)
    ppil map { ppi => 
      {
        cs.find(_.id == ppi.CampaignID) map { c =>
          BannerPhrase.select(c, ppi.BannerID, ppi.PhraseID, 1) map
            { bp =>
              dao.updatePrices(bp.id, ppi.Price)
              true
            } getOrElse false
        } getOrElse false
      }
    } find (!_) isDefined
  }
}