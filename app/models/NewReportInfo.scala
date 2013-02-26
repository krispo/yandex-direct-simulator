package models

import scala.xml._

/* ----- method CreateNewReport ------------------------------*/
/* input T */
case class NewReportInfo(
  val CampaignID: Int,
  val StartDate: String, //Date
  val EndDate: String, //Date
  val GroupByColumns: List[String] = List("clBanner", "clPhrase")) //, "clPage", "clGeo", "clPositionType"))
  {
  def toXML = {
    <NewReportInfo>
      <CampaignID>{ CampaignID }</CampaignID>
      <StartDate>{ StartDate }</StartDate>
      <EndDate>{ EndDate }</EndDate>
      {
        for (gbc <- this.GroupByColumns) yield <GroupByColumns>{ gbc }</GroupByColumns>
      }
    </NewReportInfo>
  }
}

/* output Report ID : Int,  {"data" : 123456} */
