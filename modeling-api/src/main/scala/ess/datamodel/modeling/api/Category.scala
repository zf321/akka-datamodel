package ess.datamodel.modeling.api

import java.util.UUID

import play.api.libs.json.{Format, Json}


/**
  * Category
  *
  * @param id
  * @param name
  */
case class Category(id: Option[UUID], name: String) {
}

object Category {
  implicit val format: Format[Category] = Json.format

}
