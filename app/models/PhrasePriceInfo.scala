package models

import dao.squerylorm.SquerylDao

/* method UpdatePrice -----------  use after getRecommendation ----------------------------------------------*/
/* input */
case class PhrasePriceInfo(
  val PhraseID: Int = 0,
  val BannerID: Int = 0,
  val CampaignID: Int = 0,
  val Price: Double = 0.0,
  val AutoBroker: Option[String] = Some("Yes"),
  val AutoBudgetPriority: Option[String] = Some("Medium"),
  val ContextPrice: Option[Double] = Some(0.0)) {
  def update: Boolean = {

    true

  }
}
/* output */
// {"data" : 1} if successful

object UpdatePrice {
  def update(login: String, token: String, ppil: List[PhrasePriceInfo]): Boolean = {
    val dao = new SquerylDao
    val cs = dao.getCampaigns(login)
    ppil map { ppi =>
      {
        cs.find(_.id == ppi.CampaignID) map { c =>
          c.bannerPhrases.filter(bp => (bp.banner.get.id == ppi.BannerID) & (bp.phrase.get.id == ppi.PhraseID)) match {
            case Nil => false
            case bp =>
              dao.updatePrices(bp.head.id, ppi.Price)
              true
          }
        } getOrElse false
      }
    } find (!_) isDefined
  }
}