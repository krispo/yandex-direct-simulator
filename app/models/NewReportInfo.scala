package models

/* ----- method CreateNewReport ------------------------------*/
/* input T */
case class NewReportInfo(
  val CampaignID: Int,
  val StartDate: String, //Date
  val EndDate: String, //Date
  val GroupByColumns: List[String] = List("clBanner", "clPhrase")) //, "clPage", "clGeo", "clPositionType"))
  {
  def generate: Int = {
    //generating XMLReport
    0
  }
}

/* output Report ID : Int,  {"data" : 123456} */
