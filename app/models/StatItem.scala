package models

/* method GetSummaryStat -----  for postStats ----------------------------------------------*/
/* output List[T] */
case class StatItem(
  // other parameters are not useful yet  
  val SumSearch: Double = 0.0,
  val SumContext: Double = 0.0,
  val ShowsSearch: Int = 0,
  val ShowsContext: Int = 0,
  val ClicksSearch: Int = 0,
  val ClicksContext: Int = 0)