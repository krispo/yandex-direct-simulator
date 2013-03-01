package dao.squerylorm

import org.squeryl.{ Schema, KeyedEntity }
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl._
import org.joda.time._
import java.sql.Timestamp
import scala.reflect._

@BeanInfo
case class CampaignPerformance(
  val campaign_id: Long = 0, //fk
  val periodtype_id: Long = 0, //fk
  val cost_search: Double = 0,
  val cost_context: Double = 0,
  val impress_search: Int = 0,
  val impress_context: Int = 0,
  val clicks_search: Int = 0,
  val clicks_context: Int = 0,
  val date: Timestamp = new Timestamp(0)) extends domain.Performance with KeyedEntity[Long] with History {
  val id: Long = 0

  def dateTime = date
  /**
   * put - save to db
   */
  def put(): CampaignPerformance = inTransaction { AppSchema.campaignperformance insert this }

}

object CampaignPerformance {

  /**
   * get CampaignPerformance from DB
   */
  def get_by_id(id: Long): CampaignPerformance = inTransaction {
    AppSchema.campaignperformance.where(a => a.id === id).single
  }

  def apply(c: domain.Campaign, p: domain.Performance): CampaignPerformance = CampaignPerformance(
    campaign_id = c.id,
    cost_search = p.cost_search,
    cost_context = p.cost_context,
    impress_search = p.impress_search,
    impress_context = p.impress_context,
    clicks_search = p.clicks_search,
    clicks_context = p.clicks_context,
    date = p.dateTime)

  def generate(c: domain.Campaign): CampaignPerformance = CampaignPerformance(
    campaign_id = c.id, 
    cost_search = c.bannerPhrases.map(bp => bp.performanceHistory.head.cost_search).sum,
    cost_context = c.bannerPhrases.map(bp => bp.performanceHistory.head.cost_context).sum,
    impress_search = c.bannerPhrases.map(bp => bp.performanceHistory.head.impress_search).sum,
    impress_context = c.bannerPhrases.map(bp => bp.performanceHistory.head.impress_context).sum,
    clicks_search = c.bannerPhrases.map(bp => bp.performanceHistory.head.clicks_search).sum,
    clicks_context = c.bannerPhrases.map(bp => bp.performanceHistory.head.clicks_context).sum)

}


