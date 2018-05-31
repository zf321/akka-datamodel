package ess.datamodel.modeling.impl.association

import java.util.UUID

import akka.persistence.query.Offset
import akka.stream.scaladsl.Flow
import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor.ReadSideHandler
import com.lightbend.lagom.scaladsl.persistence.{AggregateEventTag, EventStreamElement, ReadSideProcessor}
import ess.datamodel.modeling.api.Association

import scala.concurrent.{ExecutionContext, Future}

private[impl] class AssociationRepository()(implicit ec: ExecutionContext) {
  private var data: Map[UUID, Association] = Map()

  def getAssociations() = {
    Future.successful(data.values.toSeq)
  }

  def addAssociation(s: Association) = {
    data = data + (s.id.get -> s)
    Done
  }

  def getAssociation(id: UUID) = {
    data.get(id)
  }
}

private[impl] class AssociationEventProcessor(associationRep:AssociationRepository)(implicit ec: ExecutionContext)
  extends ReadSideProcessor[AssociationEvent] {
  override def buildHandler = {
    new ReadSideHandler[AssociationEvent] {

      override def prepare(tag: AggregateEventTag[AssociationEvent]): Future[Offset] = {
        Future.successful(Offset.noOffset)
      }


      override def handle(): Flow[EventStreamElement[AssociationEvent], Done, NotUsed] = {
        Flow[EventStreamElement[AssociationEvent]].mapAsync(1)(c => {
          c.event match {
            case AssociationCreated(association) => Future.successful(associationRep.addAssociation(association))
          }
        }
        )
      }
    }
  }

  override def aggregateTags = AssociationEvent.Tag.allTags
}
