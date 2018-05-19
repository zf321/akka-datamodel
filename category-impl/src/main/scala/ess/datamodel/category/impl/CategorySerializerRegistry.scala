package ess.datamodel.category.impl

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import ess.datamodel.category.api.CategoryTypeSchema

import scala.collection.immutable.Seq

object CategorySerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(

    // State
    JsonSerializer[CategoryTypeSchema],

    // Commands and replies
    JsonSerializer[CreateSchema],

    // Events
    JsonSerializer[SchemaCreated],
  )
}
