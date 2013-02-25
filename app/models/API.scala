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

  def getBanners(par: GetBannersInfo): List[BannerInfo] =
    for {
      c <- par.CampaignIDS map (dao.getCampaign(login, _).get);
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

  def getSummaryStat(par: GetSummaryStatRequest): List[StatItem] =
    par.CampaignIDS map { dao.getCampaign(login, _, par.startDate, par.endDate) get } map { c =>
      StatItem._apply(c)
    }

  def createNewReport(par: NewReportInfo): Long = {
    import scala.xml._
    val content = <a>qwe</a>

    dao.getUser(login) map { u =>
      val rep = dao.createXmlReport(u, content.toString)
      rep.id
    } getOrElse 0
  }

  def getReportList: List[ReportInfo] = {
    val conf = play.api.Play.current.configuration

    dao.getUser(login) map { u =>
      u.reports map { r =>
        ReportInfo(r.id, Some(conf.getString("uri").get + "/report/" + r.id.toString()), "Done")
      }
    } getOrElse List()

  }

  def deleteReport(par: Int): Boolean = {
    dao.deleteXmlReport(par)
    true
  }

  def updatePrices(par: List[PhrasePriceInfo]): Boolean = {
    val cs = dao.getCampaigns(login)
    par map { ppi =>
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