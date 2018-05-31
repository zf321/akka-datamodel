package ess.datamodel.modeling.impl.category

import java.util.UUID

import akka.persistence.query.Offset
import akka.stream.scaladsl.Flow
import akka.{Done, NotUsed}
import com.datastax.driver.core.utils.UUIDs
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor.ReadSideHandler
import com.lightbend.lagom.scaladsl.persistence.{AggregateEventTag, EventStreamElement, ReadSideProcessor}
import ess.datamodel.modeling.api.{Association, AssociationRule, Category}
import ess.datamodel.modeling.impl.association.AssociationRepository

import scala.concurrent.{ExecutionContext, Future}

private[impl] class CategoryRepository()(implicit ec: ExecutionContext) {
  private var data: Map[UUID, Category] = Map()

  def getCategorys() = {
    data.values.toSeq
  }

  def addCategory(s: Category) = {
    data = data + (s.id.get -> s)
  }

  def getCategory(id: UUID) = {
    data.get(id)
  }
}

private[impl] class CategoryEventProcessor(categoryRep: CategoryRepository, associationRep: AssociationRepository)(implicit ec: ExecutionContext)
  extends ReadSideProcessor[CategoryEvent] {
  override def buildHandler = {
    new ReadSideHandler[CategoryEvent] {

      override def prepare(tag: AggregateEventTag[CategoryEvent]): Future[Offset] = {
        Future.successful(Offset.noOffset)
      }


      override def handle(): Flow[EventStreamElement[CategoryEvent], Done, NotUsed] = {
        Flow[EventStreamElement[CategoryEvent]].mapAsync(1)(c => {
          c.event match {
            case CategoryCreated(category) => {
              categoryRep.addCategory(category)
              Future.successful(Done)
            }
            case CategoryChildCreated(id, category) => {
              categoryRep.addCategory(category)
              associationRep.addAssociation(Association(Some(UUIDs.timeBased()), id, category.id.get, AssociationRule.None, None, None))
              Future.successful(Done)
            }
          }
        }
        )
      }
    }
  }

  override def aggregateTags = CategoryEvent.Tag.allTags
}
