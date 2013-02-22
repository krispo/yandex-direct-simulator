package models

import play.api.libs.json._

/*----------- Response structure -------------*/
object Response {
  def apply(data: JsValue): JsValue = Json.toJson(Json.obj(("data" -> data)))
}