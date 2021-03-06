package domain

trait Region {
  def id: Long
  def parentRegion: Option[Region]
  def description: String
}
