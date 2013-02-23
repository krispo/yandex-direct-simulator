package dao.squerylorm

import org.squeryl.KeyedEntity
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl._
import scala.reflect._

@BeanInfo
case class Banner(val text: String) extends domain.Banner with KeyedEntity[Long] {
  val id: Long = 0

  // Banner -* BannerPhrase relation
  lazy val bannerPhrasesRel: OneToMany[BannerPhrase] = AppSchema.bannerBannerPhrases.left(this)

  lazy val bannerPhrases: List[BannerPhrase] = inTransaction { bannerPhrasesRel.toList }

  /**
   * default put - save to db
   */
  def put(): Banner = inTransaction { AppSchema.banners insert this }

}

object Banner {

  /**
   * select by Campaing and domain.Banner (basically network_banner_id)
   * TODO: now it's simply wrong. it has to check BP-B-Campaing association
   */
  def select(b: domain.Banner): Option[Banner] = inTransaction {
    AppSchema.banners.where(a =>
      a.id === b.id).headOption
  }

}
