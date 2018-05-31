package ess.datamodel.modeling.impl.association

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import ess.datamodel.modeling.api.Association
import ess.datamodel.utils.JsonFormats._
import play.api.libs.json.{Format, Json}


class AssociationEntity extends PersistentEntity {
  override type Command = AssociationCommand[_]
  override type Event = AssociationEvent
  override type State = Option[Association]

  override def initialState: Option[Association] = None

  override def behavior: Behavior = {

    case None => notCreated
    case _ => getAssociationCommand
  }


  private val getAssociationCommand = Actions().onReadOnlyCommand[GetAssociation.type, Option[Association]] {
    case (GetAssociation, ctx, state) => ctx.reply(state)
  }


  private val notCreated = {
    Actions().onCommand[CreateAssociation, Association] {
      case (CreateAssociation(item), ctx, state) =>
        ctx.thenPersist(AssociationCreated(item))(_ => ctx.reply(item))
    }.onEvent {
      case (AssociationCreated(item), state) => Some(item)
    }.orElse(getAssociationCommand)
  }

}

/**
  * command
  *
  * @tparam R
  */
sealed trait AssociationCommand[R] extends ReplyType[R]

case object GetAssociation extends AssociationCommand[Option[Association]] {
  implicit val format: Format[GetAssociation.type] = singletonFormat(GetAssociation)
}

case class CreateAssociation(association: Association) extends AssociationCommand[Association]

object CreateAssociation {
  implicit val format: Format[CreateAssociation] = Json.format
}


/**
  * state
  *
  */


/**
  * event
  */
sealed trait AssociationEvent extends AggregateEvent[AssociationEvent] {
  def aggregateTag = AssociationEvent.Tag
}

object AssociationEvent {
  val NumShards = 4
  val Tag = AggregateEventTag.sharded[AssociationEvent](NumShards)
}

case class AssociationCreated(association: Association) extends AssociationEvent

object AssociationCreated {
  implicit val format: Format[AssociationCreated] = Json.format
}
