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
case class CategoryTypeSchema(id: Option[UUID], name: String, code: String, isSystem: Boolean = false) {
}

object CategoryTypeSchema {
  implicit val format: Format[CategoryTypeSchema] = Json.format

  def create(
              name: String,
              code: String,
              isSystem: Boolean = false,
            ) = CategoryTypeSchema(None, name, code, isSystem)
}

/**
  * CategoryType
  *
  * @param id
  * @param name
  * @param code
  * @param parent
  * @param schema
  * @param isSystem
  */
case class CategoryType(id: Option[UUID], name: String, code: String, parent: Option[CategoryType], schema: CategoryTypeSchema, isSystem: Boolean = false) {
  def safeId = id.getOrElse(UUID.randomUUID())
}

object CategoryType {
  implicit val format: Format[CategoryType] = Json.format

  def create(
              name: String, code: String, parent: Option[CategoryType], schema: CategoryTypeSchema, isSystem: Boolean = false,
            ) = CategoryType(None, name, code, parent, schema, isSystem)
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
case class Category(id: Option[UUID], name: String, code: String, parent: Option[Category], fromDate: Date, endDate: Option[Date], categoryType: CategoryType, isSystem: Boolean = false) {
  def safeId = id.getOrElse(UUID.randomUUID())
}

object Category {
  implicit val format: Format[CategoryType] = Json.format

  def create(
              name: String, code: String, parent: Option[Category], schema: UUID, fromDate: Date, endDate: Option[Date], categoryType: CategoryType, isSystem: Boolean = false,
            ) = Category(None, name, code, parent, fromDate, endDate, categoryType, isSystem)
}

/**
  * CategoryClassification
  *
  * @param id
  * @param relateId
  * @param category
  * @param code
  * @param fromDate
  * @param endDate
  * @param isSystem
  */
case class CategoryClassification(id: Option[UUID], relateId: UUID, category: Category, code: String, fromDate: Date, endDate: Option[Date], isSystem: Boolean = false) {
  def safeId = id.getOrElse(UUID.randomUUID())
}

object CategoryClassification {
  implicit val format: Format[CategoryType] = Json.format

  def create(
              relateId: UUID, category: Category, code: String, fromDate: Date, endDate: Option[Date], isSystem: Boolean = false
            ) = CategoryClassification(None, relateId, category, code, fromDate, endDate, isSystem)
}


