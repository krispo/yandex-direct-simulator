package models

/* ----- method GetReportList --------------------------------*/
/* input T  - None.type */
/* output List[T] */

case class ReportInfo(
  val ReportID: Int,
  val Url: Option[String] = None,
  val StatusReport: String)

object ReportInfo {

  def get(login: String, token: String): ReportInfo = ReportInfo(0, None, "")
}