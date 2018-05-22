package ess.datamodel.category.api

import java.util.{Date, UUID}
import play.api.libs.json.{Format, Json}

/**
  * CategoryTypeSchema
  *
  * @param id
  * @param name
  * @param code
  * @param isSystem
  */
case class CategoryTypeSchema(id: UUID, name: String, code: String,types:Set[CategoryType] = Set[CategoryType](), isSystem: Boolean = false) {
  def addType(t:CategoryType) = {
    copy(
      types = (types + t)
    )
  }
}

object CategoryTypeSchema {
  implicit val format: Format[CategoryTypeSchema] = Json.format

  def create(
              id:UUID,
              name: String,
              code: String,
              types:Set[CategoryType] = Set[CategoryType](),
              isSystem: Boolean = false,
            ) = CategoryTypeSchema(id, name, code, types,isSystem)


}

/**
  * CategoryType
  *
  * @param id
  * @param name
  * @param code
  * @param parent
  * @param isSystem
  */
case class CategoryType(id: UUID, name: String, code: String, parent: Option[CategoryType], isSystem: Boolean = false) {
}

object CategoryType {
  implicit val format: Format[CategoryType] = Json.format

  def create(
              id:UUID,
              name: String, code: String, parent: Option[CategoryType], isSystem: Boolean = false,
            ) = CategoryType(id, name, code, parent, isSystem)
}

/**
  * Category
  *
  * @param id
  * @param name
  * @param code
  * @param parent
  * @param fromDate
  * @param endDate
  * @param isSystem
  */
case class Category(id: UUID, name: String, code: String, parent: Option[Category], fromDate: Option[Date], endDate: Option[Date], categoryType: CategoryType, isSystem: Boolean = false) {
}

object Category {
  implicit val format: Format[Category] = Json.format

  def create(
              id:UUID,
              name: String, code: String, parent: Option[Category], schema: UUID, fromDate: Option[Date], endDate: Option[Date], categoryType: CategoryType, isSystem: Boolean = false,
            ) = Category(id, name, code, parent, fromDate, endDate, categoryType, isSystem)
}
