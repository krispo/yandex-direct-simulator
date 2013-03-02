package dao.squerylorm

import org.squeryl.{ Schema, KeyedEntity, Query }
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl._
import org.squeryl.annotations.Transient
import org.joda.time._
import java.sql.Timestamp
import scala.reflect._

import domain.PositionValue._

@BeanInfo
case class BannerPhrase(
  val campaign_id: Long = 0, //fk
  val banner_id: Long = 0, //fk
  val phrase_id: Long = 0, //fk
  val region_id: Long = 0, //fk
  //////////////// prior
  val min: Double = 0,
  val max: Double = 0,
  val pMin: Double = 0,
  val pMax: Double = 0,
  val delta: Double = 0,
  val n: Long = 0) extends domain.BannerPhrase with KeyedEntity[Long] {
  val id: Long = 0

  lazy val prior: PositionValue = domain.PositionValue(min, max, pMin, pMax, delta)

  @Transient
  var campaign: Option[domain.Campaign] = None

  def banner = inTransaction { bannerRel.headOption }
  def phrase = inTransaction { phraseRel.headOption }
  def region = inTransaction { regionRel.headOption }

  // BannerPhrase History in ascending order and in conformance to campaign.historyStartDate, historyEndDate
  def getBannerPhraseHistory[T <: History](qRel: Query[T]): List[T] = campaign match {
    case None => Nil
    case Some(campaign) if (!campaign.historyStartDate.isAfter(campaign.historyEndDate)) => inTransaction {
      from(qRel)((b) =>
        where(b.date >= convertToJdbc(campaign.historyStartDate)
          and b.date <= convertToJdbc(campaign.historyEndDate.plusDays(1).minusMillis(1)))
          select (b) orderBy (b.date asc)).toList
    }
    case _ => Nil
  }

  //get History using Campaign.historyStartDate and historyEndDate
  lazy val actualBidHistory = getBannerPhraseHistory[ActualBidHistory](bannerPhraseActualBidHistoryRel)
  //lazy val price = actualBidHistory.head.bid

  lazy val netAdvisedBidsHistory = getBannerPhraseHistory[NetAdvisedBidHistory](bannerPhraseNetAdvisedBidsHistoryRel)
  /*
  def netAdvisedBidsHistory = bannerPhraseNetAdvisedBidsHistoryRel.toList map((x: NetAdvisedBidHistory) =>
      domain.TSValue(new DateTime(x.date), domain.NetAdvisedBids(x.a, x.b, x.c, x.d)))
  */

  //get History using Campaign.historyStartDate and historyEndDate
  lazy val performanceHistory = getBannerPhraseHistory[BannerPhrasePerformance](bannerPhrasePerformanceRel)

  lazy val performance = BannerPhrasePerformance(
    bannerphrase_id = id,
    cost_search = performanceHistory.map(_.cost_search).sum,
    cost_context = performanceHistory.map(_.cost_context).sum,
    impress_search = performanceHistory.map(_.impress_search).sum,
    impress_context = performanceHistory.map(_.impress_context).sum,
    clicks_search = performanceHistory.map(_.clicks_search).sum,
    clicks_context = performanceHistory.map(_.clicks_context).sum)

  // Campaign -* BannerPhrase relation
  lazy val campaignRel: ManyToOne[Campaign] = AppSchema.campaignBannerPhrases.right(this)

  // Banner -* BannerPhrase relation
  lazy val bannerRel: ManyToOne[Banner] = AppSchema.bannerBannerPhrases.right(this)

  // Phrase -* BannerPhrase relation
  lazy val phraseRel: ManyToOne[Phrase] = AppSchema.phraseBannerPhrases.right(this)

  // Region -* BannerPhrase relation
  lazy val regionRel: ManyToOne[Region] = AppSchema.regionBannerPhrases.right(this)

  // BannerPhrase -* BannerPhrasePerformance relation
  lazy val bannerPhrasePerformanceRel: OneToMany[BannerPhrasePerformance] = AppSchema.bannerPhrasePerformance.left(this)

  // BannerPhrase -* ActualBidHistory relation
  lazy val bannerPhraseActualBidHistoryRel: OneToMany[ActualBidHistory] = AppSchema.bannerPhraseActualBidHistory.left(this)

  // BannerPhrase -* NetAdvisedBidHistory relation
  lazy val bannerPhraseNetAdvisedBidsHistoryRel: OneToMany[NetAdvisedBidHistory] = AppSchema.bannerPhraseNetAdvisedBidsHistory.left(this)

  /**
   * default put - save to db
   */
  def put(): BannerPhrase = inTransaction { AppSchema.bannerphrases insert this }

}

object BannerPhrase {
  import domain.Position._
  def apply(bp: domain.BannerPhrase): BannerPhrase = BannerPhrase(
    campaign_id = bp.campaign.get.id,
    banner_id = bp.banner.get.id,
    phrase_id = bp.phrase.get.id,
    region_id = bp.region.get.id,
    min = bp.prior(_min),
    max = bp.prior(_max),
    pMin = bp.prior(_pMin),
    pMax = bp.prior(_pMax),
    delta = bp.prior(_delta),
    n = bp.n)

  /**
   * select BannerPhrase for given Campaign, banner_id, phrase_id and region_id
   * it should be 1 BannerPhrase
   * @param Campaign, String, String, String
   * @return BannerPhrase
   */
  def select(campaign: Campaign, banner_id: Long, phrase_id: Long,
    region_id: Long): Option[BannerPhrase] = inTransaction {
    val bpOpt = from(AppSchema.bannerphrases)(bp =>
      where(
        bp.campaign_id === campaign.id and
          bp.banner_id === banner_id and
          bp.phrase_id === phrase_id and
          bp.region_id === region_id) select (bp)).headOption
    bpOpt map { bp =>
      bp.campaign = Some(campaign)
      Some(bp)
    } getOrElse None
  }
}