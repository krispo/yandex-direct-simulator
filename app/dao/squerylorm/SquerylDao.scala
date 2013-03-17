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
  def updatePrices(bp_id: Long, bid: Double, dt: DateTime) =
    ActualBidHistory(bannerphrase_id = bp_id, date = dt, bid = bid).put()

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

  def generateCampaignPerformance(c: domain.Campaign, dt: DateTime): CampaignPerformance =
    CampaignPerformance.generate(c, dt).put

  def generateBudget(c: domain.Campaign, dt: DateTime): BudgetHistory =
    BudgetHistory(
      c.id,
      dt,
      c.budgetHistory.head.elem - (c.performanceHistory.head.cost_context + c.performanceHistory.head.cost_search)).put

  def clearDB: Boolean = {
    import scala.util.control.Exception._
    inTransaction {
      try {
        allCatch opt AppSchema.drop
        allCatch opt AppSchema.create

        val dt_fmt = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd")
        val date = dt_fmt.parseDateTime("2013-01-01")

        //create user
        val user = User("krisp0", "111").put

        //create campaigns
        val campaigns = List(user.campaignsRel.associate(
          Campaign(user.id, name = "Campaign_1", _login = "krisp0")))

        //BudgetHistory
        val budgetHistory = campaigns(0).budgetHistoryRel.associate(
          BudgetHistory(campaign_id = 1, date = date, budget = 10000000))

        //Banner
        val banners = List(Banner("Banner_1").put)

        //Regions
        val regions = List(Region(description = "Russia").put)

        //BannerPhrase
        val bannerPhrases =
          for (
            c <- campaigns;
            b <- banners;
            r <- regions
          ) yield {
            //Phrases
            val phrases = (1 to 100) map (i => Phrase("Phrase_" + i.toString + "_c" + c.id.toString + "_b" + b.id.toString).put) toList

            phrases.zipWithIndex map {
              case (ph, i) =>
                BannerPhrase(
                  campaign_id = c.id,
                  banner_id = b.id,
                  phrase_id = ph.id,
                  region_id = r.id,
                  //prior is like CTR 
                  min = 0.01 * (1 + (i + 1).toDouble / phrases.length),
                  max = 0.2 * (1 + (i + 1).toDouble / phrases.length),
                  pMin = 0.3 * (1 + (i + 1).toDouble / phrases.length),
                  pMax = 0.5 * (1 + (i + 1).toDouble / phrases.length),
                  delta = 0.1,
                  //cumulative traffic during the day
                  n = 100000).put
            }
          }

        true
      } catch {
        case e => false
      }
    }
  }
}