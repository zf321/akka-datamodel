package ess.datamodel.modeling.impl.category

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import ess.datamodel.modeling.api.Category

import scala.collection.immutable.Seq

object CategorySerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(

    // State
    JsonSerializer[Category],

    // Commands and replies
    JsonSerializer[CreateCategory],

    // Events
    JsonSerializer[CategoryCreated],
  )
}
