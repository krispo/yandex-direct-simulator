package dao.squerylorm

import org.squeryl._
import org.squeryl.PrimitiveTypeMode._
import scala.reflect._

@BeanInfo
case class User(
  val name: String = "",
  val password: String = "") extends domain.User with KeyedEntity[Long] {
  val id: Long = 0

  @transient
  lazy val campaignsRel = AppSchema.userCampaign.left(this)

  @transient
  lazy val reportsRel = AppSchema.userReport.left(this)

  lazy val reports: List[domain.Report] = inTransaction {
    reportsRel.toList map {
      x => { x.user = Some(this); x }
    }
  }

  /**
   * default put - save to db
   */
  def put(): User = inTransaction { AppSchema.users insert this }

}
object User {
  def select(name: String): Option[User] = inTransaction {
    AppSchema.users.where(
      a => a.name === name).headOption
  } //.single }

  def select(name: String, password: String): Option[User] = inTransaction {
    AppSchema.users.where(
      a => (a.name === name) and (a.password === password)).headOption
  }

  def create(user: domain.User): User = inTransaction {
    User(user.name, user.password).put
  }
}


