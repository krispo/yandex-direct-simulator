package models

import json_api.Convert._
import play.api.libs.json._
import dao.squerylorm.{ SquerylDao, BannerPhrase }

case class API(
  val login: String,
  val token: String = "",
  val dao: SquerylDao = new SquerylDao) {

  /**------------ methods --------------**/

  //retrieve from DB
  def getCampaignsList: List[ShortCampaignInfo] = {

    val campaigns = dao.getCampaigns(login)
    campaigns map { c =>
      ShortCampaignInfo(
        CampaignID = c.id,
        Login = login,
        Name = c.name,
        StartDate = c.startDate,
        Rest = c.budget.getOrElse(0.0))
    }
  }
  

  def getBanners(gbi: GetBannersInfo): List[BannerInfo] =
    for {
      c <- gbi.CampaignIDS map (dao.getCampaign(login, _).get);
      b <- c.banners
    } yield {
      BannerInfo(
        BannerID = b.id,
        Text = b.text,
        Geo = "",
        Phrases = c.bannerPhrases.filter(_.banner.get.id == b.id) map { bp =>
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
    

  def getSummaryStat(gssr: GetSummaryStatRequest): List[StatItem] =
    gssr.CampaignIDS map { dao.getCampaign(login, _, gssr.startDate, gssr.endDate) get } map { c =>
      StatItem._apply(c)
    }

    
  def createNewReport(par: NewReportInfo): Int = 0

  
  def getReportList: ReportInfo = ReportInfo(0, None, "")

  
  def deleteReport(par: Int): Boolean = true

  
  def updatePrices(ppil: List[PhrasePriceInfo]): Boolean = {
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