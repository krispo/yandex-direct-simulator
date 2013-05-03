package models

/* ClientInfo */
case class ClientInfo(
  val Login: String,
  //val DateCreate: DateTime,
  //val Phone: Option[String] = None,
  val FIO: String = "",
  val Email: String = "",
  val Role: String)