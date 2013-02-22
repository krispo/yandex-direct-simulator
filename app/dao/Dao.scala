package dao

import scala.collection.JavaConversions._
import java.util.{ Map => JMap, List => JList }
import org.joda.time._

import domain._

/**
 * Trait Dao
 */
trait Dao {
  /**
   * Shallow copies of Campaigns. Fast DB retrieval
   */
  def getCampaign(userName: String,  campaignId: Long,
    historyStartDate: DateTime = new DateTime, historyEndDate: DateTime = new DateTime): Option[Campaign]

  /**
   * Shallow copies of Campaigns for given user. Fast DB retrieval
   */
  def getCampaigns(userName: String): List[Campaign]


  /**
   * creates CampaignPerformance in DB
   */
  def createCampaignPerformanceReport(campaign: Campaign, performance: Performance): Performance

  /**
   * creates BannerPhrasePerformance records in DB
   * creates new BannerPhrase in case it's not present in DB.
   * @throw java.util.RunTimeException
   * TODO: add Exception checking in Controllers
   */
  def createBannerPhrasesPerformanceReport(campaign: Campaign, report: Map[BannerPhrase, Performance]): Boolean

  /**
   * createBannerPhrasesPerformanceReport with java.util.Map[BannerPhrase, Performance]
   */
  def createBannerPhrasesPerformanceReport(campaign: Campaign, report: JMap[BannerPhrase, Performance]): Boolean =
    createBannerPhrasesPerformanceReport(campaign, report.toMap)

  /**
   * creates BannerPhrase NetAdvisedBids and ActualBidHistory records in DB
   * creates new BannerPhrase in case it's not present in DB.
   * @throw java.util.RunTimeException
   */
  def createBannerPhraseNetAndActualBidReport(campaign: Campaign,
    report: Map[BannerPhrase, (ActualBidHistoryElem, NetAdvisedBids)]): Boolean

}
