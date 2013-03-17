package models

case class BannersStatItem(
  //val StatDate: DateTime = new DateTime,
  val BannerID: Int = 0,
  val PhraseID: Option[Int] = Some(0),
  //val RubricID: Int = 0,
  val Phrase: String = "",
  val Sum: Double = 0,
  val SumSearch: Double = 0,
  val SumContext: Double = 0,
  val Clicks: Int = 0,
  val ClicksSearch: Int = 0,
  val ClicksContext: Int = 0,
  val Shows: Int = 0,
  val ShowsSearch: Int = 0,
  val ShowsContext: Int = 0)