package models

import json_api.Convert._
import play.api.libs.json._
import dao.squerylorm.{ SquerylDao, BannerPhrase }

import scala.xml._

import java.text._
import org.joda.time.DateTime

case class API(
  val login: String,
  val token: String = "",
  val dao: SquerylDao = new SquerylDao) {

  val date_fmt = new SimpleDateFormat("yyyy-MM-dd")

  /**------------ methods --------------**/

  /* GetClientInfo */
  def getClientInfo: List[ClientInfo] = {
    val ci = ClientInfo(Login = login, Role = "Client")
    List(ci)
  }

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

  //ActualBids and NetAdvisedBids
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

  //Campaigns Performance DURING the day
  def getSummaryStat(par: GetSummaryStatRequest): List[StatItem] =
    par.CampaignIDS map { dao.getCampaign(login, _, par.startDate, par.endDate) get } map { c =>
      StatItem._apply(c)
    }

  //Banners Performance DURING the day
  def getBannersStat(par: NewReportInfo): GetBannersStatResponse =
    dao.getCampaign(
      login,
      par.CampaignID,
      new DateTime(date_fmt.parse(par.StartDate)),
      new DateTime(date_fmt.parse(par.EndDate))) map { c =>
        val bsiL = c.bannerPhrases map { bp =>
          BannersStatItem(
            BannerID = bp.banner.get.id,
            PhraseID = Some(bp.phrase.get.id),
            Phrase = bp.phrase.get.phrase,
            Sum = bp.performance.cost_search + bp.performance.cost_context,
            SumSearch = bp.performance.cost_search,
            SumContext = bp.performance.cost_context,
            Clicks = bp.performance.clicks_search + bp.performance.clicks_context,
            ClicksSearch = bp.performance.clicks_search,
            ClicksContext = bp.performance.clicks_context,
            Shows = bp.performance.impress_search + bp.performance.impress_context,
            ShowsSearch = bp.performance.impress_search,
            ShowsContext = bp.performance.impress_context)
        }
        GetBannersStatResponse(
          CampaignID = par.CampaignID,
          StartDate = par.StartDate,
          EndDate = par.EndDate,
          Stat = bsiL)
      } get

  //Create new detailed XML report
  def createNewReport(par: NewReportInfo): Long = {

    val content = par.toXML //save to DB the NewReportInfo      

    dao.getUser(login) map { u =>
      val rep = dao.createXmlReport(u, content.toString)
      rep.id
    } getOrElse 0
  }

  //Get Url for XML report
  def getReportList: List[ReportInfo] = {
    val conf = play.api.Play.current.configuration

    dao.getUser(login) map { u =>
      u.reports map { r =>
        ReportInfo(r.id, Some(conf.getString("uri").get + "/report/" + r.id.toString()), "Done")
      }
    } getOrElse List()

  }

  //Get XML Report for bannerPhrases at the END of the day
  def getXml(reportId: Long): Elem = {

    val report = dao.getXmlReport(reportId)
    val xml_nri = XML.loadString(report.content)

    val cID = (xml_nri \ "CampaignID").text.toLong
    val sdate = (xml_nri \ "StartDate").text //date_fmt.parse()
    val edate = (xml_nri \ "EndDate").text

    val c = dao.getCampaign(
      dao.getUser(report.user_id).name,
      cID,
      new DateTime(date_fmt.parse(sdate)),
      new DateTime(date_fmt.parse(edate))).get

    val xmlReport =
      <report>
        <reportID>{ reportId }</reportID>
        <campaignID>{ cID }</campaignID>
        <startDate>{ sdate }</startDate>
        <endDate>{ edate }</endDate>
        <phrasesDict>
          {
            for (bp <- c.bannerPhrases) yield <phrase type="phrase" phraseID={ bp.phrase.get.id.toString } value={ bp.phrase.get.phrase }/>
          }
        </phrasesDict>
        <stat>
          {
            for (bp <- c.bannerPhrases) yield <row bannerID={ bp.banner.get.id.toString } phraseID={ bp.phrase.get.id.toString } phrase_id={ bp.phrase.get.id.toString } sum_search={ bp.performance.cost_search.toString } sum_context={ bp.performance.cost_context.toString } shows_search={ bp.performance.impress_search.toString } shows_context={ bp.performance.impress_context.toString } clicks_search={ bp.performance.clicks_search.toString } clicks_context={ bp.performance.clicks_context.toString } regionID={ bp.region.get.id.toString }/>
          }
        </stat>
      </report>

    xmlReport
  }

  def deleteReport(par: Int): Int = dao.deleteXmlReport(par)

  //Set new bids
  def updatePrices(par: List[PhrasePriceInfo]): Boolean = {
    import org.joda.time.DateTime
    val dt = DateTime.now()
    val cs = dao.getCampaigns(login)
    par map { ppi =>
      {
        cs.find(_.id == ppi.CampaignID) map { c =>
          BannerPhrase.select(c, ppi.BannerID, ppi.PhraseID, 1) map
            { bp =>
              dao.updatePrices(bp.id, ppi.Price, dt)
              true
            } getOrElse false
        } getOrElse false
      }
    } find (!_) isDefined
  }
}