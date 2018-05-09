package ess.datamodel.domain.entity

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import play.api.libs.json.{Format, Json}

import scala.collection.immutable.Seq


class Entity extends PersistentEntity {
  override type Command = EntityCommand[_]
  override type Event = EntityEvent
  override type State = EntityState

  override def initialState: EntityState = EntityState("1")

  override def behavior: Behavior = {
    case EntityState(_) => Actions()
      .onReadOnlyCommand[queryEntity, String] {
      case (queryEntity(id), ctx, state) =>
        ctx.reply(id)
    }
  }

}

sealed trait EntityCommand[R] extends ReplyType[R]

case class queryEntity(id: String) extends EntityCommand[String]

object queryEntity {
  implicit val format: Format[queryEntity] = Json.format
}

case class EntityState(id: String)

sealed trait EntityEvent extends AggregateEvent[EntityEvent] {
  def aggregateTag = EntityEvent.Tag
}

object EntityEvent {
  val Tag = AggregateEventTag[EntityEvent]
}

object EntitySerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[queryEntity]
  )
}
