package ess.datamodel.modeling.impl.category

import java.util.UUID

import akka.persistence.query.Offset
import akka.stream.scaladsl.Flow
import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor.ReadSideHandler
import com.lightbend.lagom.scaladsl.persistence.{AggregateEventTag, EventStreamElement, ReadSideProcessor}
import ess.datamodel.modeling.api.Category

import scala.concurrent.{ExecutionContext, Future}

private[impl] class CategoryRepository()(implicit ec: ExecutionContext) {
  private var data: Map[UUID, Category] = Map()

  def getCategorys() = {
    Future.successful(data.values.toSeq)
  }

  def addCategory(s: Category) = {
    data = data + (s.id.get -> s)
    Done
  }

  def getCategory(id: UUID) = {
    data.get(id)
  }
}

private[impl] class CategoryEventProcessor(rep: CategoryRepository)(implicit ec: ExecutionContext)
  extends ReadSideProcessor[CategoryEvent] {
  override def buildHandler = {
    new ReadSideHandler[CategoryEvent] {

      override def prepare(tag: AggregateEventTag[CategoryEvent]): Future[Offset] = {
        Future.successful(Offset.noOffset)
      }


      override def handle(): Flow[EventStreamElement[CategoryEvent], Done, NotUsed] = {
        Flow[EventStreamElement[CategoryEvent]].mapAsync(1)(c => {
          c.event match {
            case CategoryCreated(category) => Future.successful(rep.addCategory(category))
          }
        }
        )
      }
    }
  }

  override def aggregateTags = CategoryEvent.Tag.allTags
}
