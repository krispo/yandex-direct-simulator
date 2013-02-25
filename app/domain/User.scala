package domain

import scala.collection.JavaConversions._
import java.util.{ Map => JMap, List => JList }

//import scala.reflect._

//@BeanInfo
trait User {
  def id: Long
  def name: String
  def password: String

  def reports: List[Report]
  @transient lazy val reportsJList: JList[Report] = reports
}

