package dao.squerylorm

import org.squeryl.{ Schema, KeyedEntity, Query }
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.annotations.Transient

import scala.xml._

case class Report(
  val user_id: Long, //fk
  val content: String = "" //Node = <a> Hello world !</a> //xml
  ) extends domain.Report with KeyedEntity[Long] {

  val id: Long = 0

  @Transient
  var user: Option[domain.User] = None

  // Campaign -* BannerPhrase relation
  lazy val userRel = AppSchema.userReport.right(this) //won't need

  /**
   * default put - save to db
   */
  def put(): Report = inTransaction { AppSchema.reports insert this }
}

object Report {
  def get_by_id(id: Long): Report = inTransaction { AppSchema.reports.where(a => a.id === id).single }
}