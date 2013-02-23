package domain

import scala.collection.JavaConversions._
import java.util.{ List => JList }

trait Banner {
  def id: Long
  def text: String
}
