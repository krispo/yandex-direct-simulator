package models

import java.text._
import org.joda.time.DateTime

/* method GetSummaryStat -----  for postStats ----------------------------------------------*/
/* input T */
case class GetSummaryStatRequest(
  val CampaignIDS: List[Int],
  val StartDate: String, //Date
  val EndDate: String) {

  /* Date format */
  val date_fmt = new SimpleDateFormat("yyyy-MM-dd")

  def startDate: DateTime = new DateTime(date_fmt.parse(StartDate))
  def endDate: DateTime = new DateTime(date_fmt.parse(EndDate))
}
