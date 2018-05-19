package ess.datamodel.category.impl

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import ess.datamodel.category.api.CategoryTypeSchema
import ess.datamodel.utils.JsonFormats._
import play.api.libs.json.{Format, Json}


class CategoryEntity extends PersistentEntity {
  override type Command = CategoryCommand[_]
  override type Event = CategoryEvent
  override type State = Option[CategoryTypeSchema]

  override def initialState: Option[CategoryTypeSchema] = None

  override def behavior: Behavior = {

    case None => notCreated
  }


  private val getSchemaCommand = Actions().onReadOnlyCommand[GetSchema.type, Option[CategoryTypeSchema]] {
    case (GetSchema, ctx, state) => ctx.reply(state)
  }


  private val notCreated = {
    Actions().onCommand[CreateSchema, CategoryTypeSchema] {
      case (CreateSchema(item), ctx, state) =>
        ctx.thenPersist(SchemaCreated(item))(_ => ctx.reply(item))
    }.onEvent {
      case (SchemaCreated(item), state) => Some(item)
    }.orElse(getSchemaCommand)
  }
}

/**
  * command
  *
  * @tparam R
  */
sealed trait CategoryCommand[R] extends ReplyType[R]

case object GetSchema extends CategoryCommand[Option[CategoryTypeSchema]] {
  implicit val format: Format[GetSchema.type] = singletonFormat(GetSchema)
}

case class CreateSchema(schema: CategoryTypeSchema) extends CategoryCommand[CategoryTypeSchema]

object CreateSchema {
  implicit val format: Format[CreateSchema] = Json.format
}

/**
  * state
  *
  */


/**
  * event
  */
sealed trait CategoryEvent extends AggregateEvent[CategoryEvent] {
  def aggregateTag = CategoryEvent.Tag
}

object CategoryEvent {
  val NumShards = 4
  val Tag = AggregateEventTag.sharded[CategoryEvent](NumShards)
}

case class SchemaCreated(schema: CategoryTypeSchema) extends CategoryEvent

object SchemaCreated {
  implicit val format: Format[SchemaCreated] = Json.format
}