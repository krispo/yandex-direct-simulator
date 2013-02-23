package domain

import scala.reflect._
import org.joda.time._
import scala.collection.JavaConversions._
import java.util.{ Map => JMap, List => JList }

@BeanInfo
trait Campaign {
  def id: Long
  def startDate: DateTime
  def endDate: Option[DateTime]
  def budget: Option[Double]

  def user: Option[User]
  def login: Option[String]
  def token: Option[String]

  def bannerPhrases: List[BannerPhrase]
  @transient lazy val bannerPhrasesJList: JList[BannerPhrase] = bannerPhrases

  def banners: List[Banner]
  @transient lazy val bannerJList: JList[Banner] = banners

  // start and end Dates of retrieved Campaign Histories
  //@BeanProperty
  def historyStartDate: DateTime
  //@BeanProperty
  def historyEndDate: DateTime

  def performanceHistory: List[Performance]
  @transient lazy val performanceHistoryJList: JList[Performance] = performanceHistory

  def performance: Performance

  def budgetHistory: List[BudgetHistoryElem]
  @transient lazy val budgetHistoryJList: JList[BudgetHistoryElem] = budgetHistory
}

