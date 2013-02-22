package domain

import org.joda.time.DateTime
import scala.collection.JavaConversions._
import java.util.{Map => JMap, List => JList}


trait BannerPhrase{
  def id: Long
  def campaign: Option[Campaign]
  def banner: Option[Banner]
  def phrase: Option[Phrase]
  def region: Option[Region]
  def actualBidHistory: List[ActualBidHistoryElem]
  lazy val actualBidHistoryJList: JList[ActualBidHistoryElem] = actualBidHistory
  def netAdvisedBidsHistory: List[NetAdvisedBids]
  lazy val netAdvisedBidsHistoryJList: JList[NetAdvisedBids] = netAdvisedBidsHistory
  def performanceHistory: List[Performance]
  lazy val performanceHistoryJList: JList[Performance] = performanceHistory
}

