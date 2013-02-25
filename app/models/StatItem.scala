package models

import dao.squerylorm.SquerylDao

/*use for whole CAMPAIGN*/
/* method GetSummaryStat -----  for postStats ----------------------------------------------*/
/* output List[T] */
case class StatItem(
  // other parameters are not useful yet 
  val CampaignID: Long = 0,
  val SumSearch: Double = 0.0,
  val SumContext: Double = 0.0,
  val ShowsSearch: Int = 0,
  val ShowsContext: Int = 0,
  val ClicksSearch: Int = 0,
  val ClicksContext: Int = 0)

object StatItem {

  def _apply(c: dao.squerylorm.Campaign): StatItem = {
    val p = c.performance
    StatItem(
      CampaignID = c.id,
      SumSearch = p.cost_search,
      SumContext = p.cost_context,
      ShowsSearch = p.impress_search,
      ShowsContext = p.impress_context,
      ClicksSearch = p.clicks_search,
      ClicksContext = p.clicks_context)
  }
}