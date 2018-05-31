package ess.datamodel.modeling.impl

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import ess.datamodel.modeling.api.{Association, Category}
import ess.datamodel.modeling.impl.association.{AssociationCreated, CreateAssociation}
import ess.datamodel.modeling.impl.category.{CategoryCreated, CreateCategory}

import scala.collection.immutable.Seq

object ModelingSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(

    //category
    // State
    JsonSerializer[Category],

    // Commands and replies
    JsonSerializer[CreateCategory],

    // Events
    JsonSerializer[CategoryCreated],



    //association
    // State
    JsonSerializer[Association],

    // Commands and replies
    JsonSerializer[CreateAssociation],

    // Events
    JsonSerializer[AssociationCreated],
  )
}
