package dao.squerylorm

import org.squeryl.{ Schema, KeyedEntity, Query }
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl._
import org.squeryl.annotations.Transient
import java.lang.RuntimeException
import org.joda.time._
import java.sql.Timestamp
import scala.reflect._
import org.squeryl.ForeignKeyDeclaration

@BeanInfo
case class Campaign(
  var user_id: Long = 0, //fk
  val name: String = "",
  val start: Timestamp = new Timestamp(1),
  val _login: String = "",
  val _token: String = "") extends domain.Campaign with KeyedEntity[Long] {
  val id: Long = 0
  def startDate = start

  @Transient
  var historyStartDate: DateTime = sdate //new DateTime
  @Transient
  var historyEndDate: DateTime = edate //new DateTime

  def endDate = Some(historyEndDate)
  /*
  // Campaign -* Banner relation
  lazy val bannersRel: OneToMany[Banner] = AppSchema.campaignBanners.left(this)
  */

  // Campaign -* BannerPhrase relation
  lazy val bannerPhrasesRel: OneToMany[BannerPhrase] = AppSchema.campaignBannerPhrases.left(this)

  // Campaign -* CampaignPerformance relation
  lazy val performancesRel: OneToMany[CampaignPerformance] = AppSchema.campaignPerformance.left(this)

  // Campaign -* BudgetHistory relation
  lazy val budgetHistoryRel: OneToMany[BudgetHistory] = AppSchema.campaignBudgetHistory.left(this)

  // Campaign History in ascending order and in conformance to campaign.historyStartDate, historyEndDate
  def getHistory[T <: History](qRel: Query[T]): List[T] = if (!historyStartDate.isAfter(historyEndDate)) inTransaction {
    from(qRel)((b) =>
      where(b.date >= convertToJdbc(historyStartDate)
        and b.date <= convertToJdbc(historyEndDate.plusDays(1).minusMillis(1)))
        select (b) orderBy (b.date asc)).toList
  }
  else Nil

  lazy val budgetHistory = getHistory[BudgetHistory](budgetHistoryRel)
  def budget = budgetHistory.lastOption map (_.budget)

  def user = None
  def login = Some(_login)
  def token = Some(_token)

  //get_bannerphrases and assign Campaign (with historyStartDate, historyEndDate)
  lazy val bannerPhrases: List[domain.BannerPhrase] = inTransaction {
    bannerPhrasesRel.toList map (
      x => { x.campaign = Some(this); x })
  }

  lazy val banners: List[domain.Banner] = bannerPhrases map { _.banner.get } distinct // List(1,1,2,3,3) => List(1,2,3)

  lazy val performanceHistory = getHistory[CampaignPerformance](performancesRel)

  lazy val performance = CampaignPerformance(
    campaign_id = id,
    cost_search = performanceHistory.map(_.cost_search).sum,
    cost_context = performanceHistory.map(_.cost_context).sum,
    impress_search = performanceHistory.map(_.impress_search).sum,
    impress_context = performanceHistory.map(_.impress_context).sum,
    clicks_search = performanceHistory.map(_.clicks_search).sum,
    clicks_context = performanceHistory.map(_.clicks_context).sum)

  /**
   * default put - save to db
   */
  def put(): Campaign = inTransaction { AppSchema.campaigns insert this }

  def createBannerPhrasesPerformanceReport(report: Map[domain.BannerPhrase, domain.Performance]): Boolean = inTransaction {
    // f saves domain.Histories for BannerPhrase
    def f(performance: domain.Performance)(bp: BannerPhrase) = {
      val bp_perf = BannerPhrasePerformance((bp, performance)).put
      require(bp_perf.id != 0)
      require(bp_perf.bannerphrase_id == bp.id)
    }
    val bp_history = report map { case (bp, p) => bp -> f(p)_ }
    createBannerPhraseHistory(bp_history)
  }

  /**
   * creates ActualBidHistory and NetAdvisedBidHistory records
   * creates new Banners, Phrases, Regions and BannerPhrase if needed
   * @throw java.lang.RuntimeException - if Report is malformed - nothing created
   */
  def createActualBidAndNetAdvisedBids(report: Map[domain.BannerPhrase, (domain.ActualBidHistoryElem, domain.NetAdvisedBids)]): Boolean =
    inTransaction {
      // f saves domain.Histories for BannerPhrase
      def f(pair: (domain.ActualBidHistoryElem, domain.NetAdvisedBids))(bp: BannerPhrase) = {
        val (abid, netbid) = pair
        require((ActualBidHistory(bp, abid).put).bannerphrase_id == bp.id)
        require((NetAdvisedBidHistory(bp, netbid).put).bannerphrase_id == bp.id)
      }
      val bp_history = report map { case (bp, pair) => bp -> f(pair)_ }
      createBannerPhraseHistory(bp_history)
    }

  /**
   * creates History records as (BannerPhrase)=>
   * creates new Banners, Phrases, Regions and BannerPhrase if needed
   * @throw java.lang.RuntimeException - if Report is malformed - nothing created
   */
  def createBannerPhraseHistory(report: Map[domain.BannerPhrase, (BannerPhrase) => Unit]): Boolean = inTransaction {
    val res = report.map { case (bp, f) => f(BannerPhrase(bp)) }
    res.nonEmpty
  }

}

object Campaign {

  /**
   * get Campaign from DB
   */
  def get_by_id(id: Long): Campaign = inTransaction { AppSchema.campaigns.where(a => a.id === id).single }

  /**
   * select Campaigns for given user_name, network_name and network_campaign_id
   * it should be 1 Campaign generally
   */
  def select(user_name: String, campaign_id: Long): List[Campaign] = inTransaction {
    from(AppSchema.campaigns, AppSchema.users)((c, u) =>
      where(c.id === campaign_id
        and u.name === user_name
        and c.user_id === u.id)
        select (c)).toList
  }

  /**
   * select Campaigns for given user_name, network_name
   */
  def select(user_name: String): List[Campaign] = inTransaction {
    from(AppSchema.campaigns, AppSchema.users)((c, u) =>
      where(
        u.name === user_name and c.user_id === u.id)
        select (c)).toList
  }

  /**
   * select all campaigns of User
   */
  def select(user: User): List[Campaign] = inTransaction {
    from(AppSchema.campaigns)((c) => where(c.user_id === user.id) select (c)).toList
  }

  /**
   * creates Campaign record
   */
  def create(cc: domain.Campaign): domain.Campaign = inTransaction {
    // create DB Campaign
    val c = Campaign(
      user_id = cc.user.get.id,
      start = cc.startDate,
      _login = cc.login.getOrElse(""),
      _token = cc.token.getOrElse("")).put
    // create BudgetHistory
    val budgetHistory = BudgetHistory(
      campaign_id = c.id,
      date = c.start,
      budget = cc.budget.get).put

    // return DB Campaign
    c
  }
}