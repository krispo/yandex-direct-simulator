package models

/* ----- method GetReportList --------------------------------*/
/* input T  - None.type */
/* output List[T] */

case class ReportInfo(
  val ReportID: Long,
  val Url: Option[String] = None,
  val StatusReport: String)