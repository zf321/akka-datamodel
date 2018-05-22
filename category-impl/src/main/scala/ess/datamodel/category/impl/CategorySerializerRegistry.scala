package ess.datamodel.category.impl

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import ess.datamodel.category.api.{Category, CategoryType, CategoryTypeSchema}

import scala.collection.immutable.Seq

object CategorySerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(

    // State
    JsonSerializer[CategoryTypeSchema],
    JsonSerializer[CategoryType],
    JsonSerializer[Category],

    // Commands and replies
    JsonSerializer[CreateSchema],
    JsonSerializer[AddType],

    // Events
    JsonSerializer[SchemaCreated],
    JsonSerializer[TypeAdded],
  )
}
