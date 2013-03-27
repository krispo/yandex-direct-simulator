package dao.squerylorm

import org.joda.time.DateTime

object Charts {

  //Budget evolution in time
  def getBudget(oc: Option[Campaign]): List[(Long, Double)] =
    oc map {
      c => c.budgetHistory.map(bh => (bh.date.getMillis(), bh.budget)).reverse.tail
    } getOrElse (Nil)

  //Campaign CTR evolution in time with cumulative clicks and shows
  def getCampaignCTR(oc: Option[Campaign]): List[(Long, Double, Double, Double)] = {
    oc map { c =>
      val cp = c.performanceHistory.reverse
      val cClicksContext = cp.map(_.clicks_context).scan(0)(_ + _).tail
      val cClicksSearch = cp.map(_.clicks_search).scan(0)(_ + _).tail
      val cShowsContext = cp.map(_.impress_context).scan(0)(_ + _).tail
      val cShowsSearch = cp.map(_.impress_search).scan(0)(_ + _).tail

      val p = cp.map(_.date).zipWithIndex

      p map {
        case (dt, i) =>
          (dt.getMillis(), //DateTime
            ctr(cClicksSearch(i), cShowsSearch(i)), //CTR search
            ctr(cClicksContext(i), cShowsContext(i)), //CTR context
            ctr(cClicksSearch(i) + cClicksContext(i), cShowsSearch(i) + cShowsContext(i)) //CTR SUM
            )
      }
    } getOrElse (Nil)
  }

  //CTR function
  def ctr(cl: Int, sh: Int): Double = {
    if (sh != 0)
      cl.toDouble / sh.toDouble
    else
      0
  }
}