package dao.squerylorm

import org.squeryl.{ Schema, KeyedEntity }
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
  val date: Timestamp = new Timestamp(0)) extends domain.Performance with KeyedEntity[Long] with History {
  val id: Long = 0

  def dateTime: DateTime = new DateTime(date)

  // BannerPhrase -* BannerPhrasePerformance relation
  lazy val bannerPhraseRel: ManyToOne[BannerPhrase] = AppSchema.bannerPhrasePerformance.right(this)

  /**
   * put - save to db
   */
  def put(): BannerPhrasePerformance = inTransaction { AppSchema.bannerphraseperformance insert this }

}

object BannerPhrasePerformance {

  /**
   * get BannerPhrasePerformance from DB
   */
  def get_by_id(id: Long): BannerPhrasePerformance = inTransaction {
    AppSchema.bannerphraseperformance.where(a => a.id === id).single
  }

  def apply(t: (domain.BannerPhrase, domain.Performance)): BannerPhrasePerformance =
    BannerPhrasePerformance(
      bannerphrase_id = t._1.id,
      cost_search = t._2.cost_search,
      cost_context = t._2.cost_context,
      impress_search = t._2.impress_search,
      impress_context = t._2.impress_context,
      clicks_search = t._2.clicks_search,
      clicks_context = t._2.clicks_context,
      date = t._2.dateTime)

  import domain.Position._
  import domain.PositionValue._
  def generate(bp: domain.BannerPhrase, ts: Timestamp): BannerPhrasePerformance = {
    /**
     * q - shows(or impress) probability
     * p - clicks prior probability
     * m - traffic
     * impress ~ Binomial(traffic,q)
     * clicks ~ Binomial(impress,p)
     * costs = clicks*bid   (strictly speaking, costs < clicks*bid)
     */
    import org.apache.commons.math3.random.RandomDataGenerator
    val r = new RandomDataGenerator()

    val nb = bp.netAdvisedBidsHistory.head
    val bid = bp.actualBidHistory.head.elem
    val dt_next = new DateTime(ts)
    val dt_prev = bp.performanceHistory.headOption.map(p => p.dateTime).getOrElse(dt_next.minusMinutes(dt_next.getMinuteOfDay()))

    def bpp(positionValue: Double, position: Position) = {
      val q = if (bid < positionValue + bp.prior(_delta)) (bid - positionValue + 0.1) / (bp.prior(_delta) + 0.1) else 1
      val shows = r.nextBinomial((bp.n * trafficDiff(dt_prev, dt_next)).toInt, q)
      val clicks = r.nextBinomial(shows, bp.prior(position))

      BannerPhrasePerformance(
        bannerphrase_id = bp.id,
        cost_search = clicks * bid,
        cost_context = clicks * bid,
        impress_search = shows,
        impress_context = shows,
        clicks_search = clicks,
        clicks_context = clicks,
        date = ts)
    }

    if (bid < nb.a) BannerPhrasePerformance(bannerphrase_id = bp.id, date = ts) //_bottom
    else if (bid < nb.b) bpp(nb.a, _min) //_min
    else if (bid < nb.c) bpp(nb.b, _max) //_max
    else if (bid < nb.d) bpp(nb.c, _pMin) //_pMin
    else bpp(nb.d, _pMax) //_pMax
  }
}