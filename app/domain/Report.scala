package domain

import scala.xml.Node

trait Report {
  def id: Long 
  def content: String//Node

  def user: Option[User]
}