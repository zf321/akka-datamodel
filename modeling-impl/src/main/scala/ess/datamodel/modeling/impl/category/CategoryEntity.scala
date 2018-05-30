package ess.datamodel.modeling.impl.category

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import ess.datamodel.modeling.api.Category
import ess.datamodel.utils.JsonFormats._
import play.api.libs.json.{Format, Json}


class CategoryEntity extends PersistentEntity {
  override type Command = CategoryCommand[_]
  override type Event = CategoryEvent
  override type State = Option[Category]

  override def initialState: Option[Category] = None

  override def behavior: Behavior = {

    case None => notCreated
    case _ => getCategoryCommand
  }


  private val getCategoryCommand = Actions().onReadOnlyCommand[GetCategory.type, Option[Category]] {
    case (GetCategory, ctx, state) => ctx.reply(state)
  }


  private val notCreated = {
    Actions().onCommand[CreateCategory, Category] {
      case (CreateCategory(item), ctx, state) =>
        ctx.thenPersist(CategoryCreated(item))(_ => ctx.reply(item))
    }.onEvent {
      case (CategoryCreated(item), state) => Some(item)
    }.orElse(getCategoryCommand)
  }

}

/**
  * command
  *
  * @tparam R
  */
sealed trait CategoryCommand[R] extends ReplyType[R]

case object GetCategory extends CategoryCommand[Option[Category]] {
  implicit val format: Format[GetCategory.type] = singletonFormat(GetCategory)
}

case class CreateCategory(category: Category) extends CategoryCommand[Category]

object CreateCategory {
  implicit val format: Format[CreateCategory] = Json.format
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

case class CategoryCreated(category: Category) extends CategoryEvent

object CategoryCreated {
  implicit val format: Format[CategoryCreated] = Json.format
}
