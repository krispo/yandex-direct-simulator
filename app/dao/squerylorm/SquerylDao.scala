package dao.squerylorm

import org.squeryl.{ Schema, KeyedEntity, Query }
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl._
import org.joda.time._
import scala.reflect._
import domain.{ Campaign => dCam, Performance => dPerf }

@BeanInfo
class SquerylDao extends dao.Dao {

  /**
   * XmlReport
   */
  def createXmlReport(user: domain.User, content: String) = Report(user.id, content).put
  def getXmlReport(reportId: Long) = Report.get_by_id(reportId)
  def deleteXmlReport(reportId: Long) = inTransaction { AppSchema.reports deleteWhere (r => r.id === reportId.toInt) }

  def getCampaign(userName: String, campaignId: Long,
    historyStartDate: DateTime = sdate, historyEndDate: DateTime = edate) =
    Campaign.select(userName: String, campaignId: Long).headOption map {
      campaign =>
        {
          campaign.historyStartDate = historyStartDate
          campaign.historyEndDate = historyEndDate
          campaign
        }
    }

  def getCampaigns(userName: String) =
    Campaign.select(userName: String)

  /**
   * creates CampaignPerformance in DB
   * TODO: Optimize. It has 2 DB trips now
   */
  def createCampaignPerformanceReport(campaign: dCam, performance: dPerf) =
    CampaignPerformance(campaign, performance).put

  /**
   * creates BannerPhrasePerformance records in DB
   * TODO: Optimize of course
   */
  def createBannerPhrasesPerformanceReport(campaign: domain.Campaign, report: Map[domain.BannerPhrase, dPerf]) =
    //BannerPhrasePerformance.create(report)
    Campaign.get_by_id(campaign.id).createBannerPhrasesPerformanceReport(report)

  /**
   * UpdatePrices
   */
  def updatePrices(bp_id: Long, bid: Double) =
    ActualBidHistory(bannerphrase_id = bp_id, bid = bid).put()

  /**
   * retrieves full domain model (Campaign and its Histories) for given Dates from DB
   * TODO: Optimize. CampaignHistory is not complete. It's done partially only
   * def getCampaignWithHistory(campaign_id: Long, historyStartDate: DateTime, historyEndDate: DateTime): domain.Campaign =
   * Campaign.selectCampaignWithHistory(campaign_id, historyStartDate, historyEndDate)
   */

  /**
   * creates Campaign record
   */
  def create(campaign: dCam) = Campaign.create(cc = campaign)

  /**
   * creates new records in EndDateHistory or BudgetHistory
   * TODO: change definition. funct. should take {new_budget: date, new_endDate: date} ...
   */
  def update(campaign: dCam, date: DateTime) = ???

  /**
   * creates new User record
   */
  def create(user: domain.User): domain.User = User.create(user)

  /**
   * updates user.name
   */
  def update(user: domain.User): domain.User = ???
  /**
   * select User by name
   */
  def getUser(id: Long): domain.User = User.get_by_id(id)
  def getUser(name: String): Option[domain.User] = User.select(name)
  def getUser(name: String, password: String): Option[domain.User] = User.select(name, password)

  /**
   * data simulator
   */
  import domain.Position._
  import domain.PositionValue._

  def generateNetAdvisedBids(bp: domain.BannerPhrase, mu: PositionValue, sigma: PositionValue, dt: DateTime): NetAdvisedBidHistory =
    NetAdvisedBidHistory.generate(bp, mu, sigma, dt).put

  def generateBannerPhrasePerformance(bp: domain.BannerPhrase, dt: DateTime): BannerPhrasePerformance =
    BannerPhrasePerformance.generate(bp, dt).put

  def generateCampaignPerformance(c: domain.Campaign): CampaignPerformance =
    CampaignPerformance.generate(c).put

  def clearDB: Boolean = {
    import scala.util.control.Exception._
    inTransaction {
      try {
        allCatch opt AppSchema.drop
        allCatch opt AppSchema.create
        true
      } catch {
        case e => false
      }
    }
  }
}