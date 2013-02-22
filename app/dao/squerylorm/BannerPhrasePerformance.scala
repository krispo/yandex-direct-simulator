package dao.squerylorm

import org.squeryl.{Schema, KeyedEntity}
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl._
import org.joda.time._
import java.sql.Timestamp
import scala.reflect._


@BeanInfo
case class BannerPhrasePerformance(
  val bannerphrase_id: Long = 0, //fk
  val periodtype_id: Long = 0, //fk
  val cost_search: Double = 0,
  val cost_context: Double = 0,
  val impress_search: Int = 0,
  val impress_context: Int = 0,
  val clicks_search: Int = 0,
  val clicks_context: Int = 0,
  val date: Timestamp = new Timestamp(0)
) extends domain.Performance with KeyedEntity[Long] with History
{
  val id: Long = 0

  def dateTime: DateTime = new DateTime(date)
 
  // BannerPhrase -* BannerPhrasePerformance relation
  lazy val bannerPhraseRel: ManyToOne[BannerPhrase] = AppSchema.bannerPhrasePerformance.right(this)

  /** put - save to db
  */
  def put(): BannerPhrasePerformance = inTransaction { AppSchema.bannerphraseperformance insert this }


}

object BannerPhrasePerformance {

  /**
  * get BannerPhrasePerformance from DB
  */
  def get_by_id(id: Long): BannerPhrasePerformance = inTransaction{
    AppSchema.bannerphraseperformance.where(a => a.id === id).single }


  def apply(t: ( domain.BannerPhrase, domain.Performance)): BannerPhrasePerformance =
    BannerPhrasePerformance(
      bannerphrase_id = t._1.id,
      cost_search = t._2.cost_search,
      cost_context = t._2.cost_context,
      impress_search = t._2.impress_search,
      impress_context = t._2.impress_context,
      clicks_search = t._2.clicks_search,
      clicks_context = t._2.clicks_context,
      date = t._2.dateTime
    )
}


