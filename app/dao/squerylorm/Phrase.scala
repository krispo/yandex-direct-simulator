package dao.squerylorm

import org.squeryl.KeyedEntity
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl._
import scala.reflect._

@BeanInfo
case class Phrase(
  val phrase: String = "") extends domain.Phrase with KeyedEntity[Long] {
  val id: Long = 0

  // Phrase -* BannerPhrase relation
  lazy val bannerPhrasesRel: OneToMany[BannerPhrase] = AppSchema.phraseBannerPhrases.left(this)

  /**
   * default put - save to db
   */
  def put(): Phrase = inTransaction { AppSchema.phrases insert this }

}

object Phrase {

  /**
   * construct Phrase from domain.Phrase
   */
  def apply(p: domain.Phrase): Phrase =
    Phrase(
      phrase = p.phrase)
}

