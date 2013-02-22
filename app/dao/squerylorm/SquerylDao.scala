package dao.squerylorm

import org.squeryl.{ Schema, KeyedEntity, Query }
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl._
import org.joda.time._
import scala.reflect._
import domain.{ Campaign => dCam, Performance => dPerf}

@BeanInfo
class SquerylDao extends dao.Dao {

  override def getCampaign(userName: String, campaignId: Long,
    historyStartDate: DateTime = new DateTime, historyEndDate: DateTime = new DateTime) =
    Campaign.select(userName: String, campaignId: Long).headOption map {
      campaign =>
        {
          campaign.historyStartDate = historyStartDate
          campaign.historyEndDate = historyEndDate
          campaign
        }
    }

  override def getCampaigns(userName: String) =
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
   * creates BannerPhrase NetAdvisedBids and ActualBidHistory records in DB
   * creates new BannerPhrase in case it's not present in DB.
   * TODO: Optimize of course
   * @throw java.util.RunTimeException
   */
  def createBannerPhraseNetAndActualBidReport(campaign: domain.Campaign,
    report: Map[domain.BannerPhrase, (domain.ActualBidHistoryElem, domain.NetAdvisedBids)]): Boolean =
    Campaign.get_by_id(campaign.id).createActualBidAndNetAdvisedBids(report)

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
  def getUser(name: String): Option[domain.User] = User.select(name)
  def getUser(name: String, password: String): Option[domain.User] = User.select(name, password)


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