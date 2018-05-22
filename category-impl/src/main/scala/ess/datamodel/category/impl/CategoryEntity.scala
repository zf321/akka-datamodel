package ess.datamodel.category.impl

import java.util.UUID

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import ess.datamodel.category.api.{CategoryType, CategoryTypeSchema}
import ess.datamodel.utils.JsonFormats._
import play.api.libs.json.{Format, Json}


class CategoryEntity extends PersistentEntity {
  override type Command = CategoryCommand[_]
  override type Event = CategoryEvent
  override type State = Option[CategoryTypeSchema]

  override def initialState: Option[CategoryTypeSchema] = None

  override def behavior: Behavior = {

    case None => notCreated
    case _ => edit
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

  private val edit = {
    Actions().onCommand[AddType, Done] {
      case (AddType(item), ctx, state) =>
        ctx.thenPersist(TypeAdded(state.get.id, item))(_ => ctx.reply(Done))
    }.onEvent {
      case (TypeAdded(id, item), state) => Some(state.get.addType(item))
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


case class AddType(t: CategoryType) extends CategoryCommand[Done]

object AddType {
  implicit val format: Format[AddType] = Json.format
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

case class TypeAdded(id: UUID, t: CategoryType) extends CategoryEvent

object TypeAdded {
  implicit val format: Format[TypeAdded] = Json.format
}