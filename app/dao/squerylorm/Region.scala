package dao.squerylorm

import org.squeryl.KeyedEntity
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl._
import scala.reflect._




@BeanInfo
case class Region(
  var region_id: Long = 0, //official region id
  var parent_id: Long = 0, //fk to super Region
  val description: String = ""
)extends domain.Region with KeyedEntity[Long]
{
  val id: Long = 0

  /** select Parent Region
  */
  def parentRegion: Option[Region] = inTransaction{
    from(AppSchema.regions)(( r ) =>
      where(r.id === parent_id)
      select(r) ).headOption
  }


  // Region -* BannerPhrase relation
  lazy val bannerPhrasesRel: OneToMany[BannerPhrase] = AppSchema.regionBannerPhrases.left(this)


  /**
  * default put - save to db
  **/
  def put(): Region = inTransaction { AppSchema.regions insert this }


}

object Region {

  /** construct Region from domain.Region
  */
  def apply(region: domain.Region): Region =
    Region(
      parent_id = region.parentRegion map(_.id) getOrElse(0)
    )
}
