package ess.datamodel.category.impl

import java.util.UUID

import akka.persistence.query.Offset
import akka.{Done, NotUsed}
import akka.stream.scaladsl.Flow
import com.lightbend.lagom.scaladsl.persistence.{AggregateEventTag, EventStreamElement, ReadSideProcessor}
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor.ReadSideHandler
import ess.datamodel.category.api.{CategoryType, CategoryTypeSchema}

import scala.concurrent.{ExecutionContext, Future}

private[impl] class CategoryRepository()(implicit ec: ExecutionContext) {
  private val data: Map[UUID, CategoryTypeSchema] = Map()

  def getSchemas() = {
    Future.successful(data.values.toSeq)
  }

  def addSchema(s: CategoryTypeSchema) = {
    data + (s.id -> s)
    Done
  }

  def getSchema(id: UUID) = {
    data.get(id)
  }

  def addType(id: UUID, t: CategoryType) = {
    var m = data.get(id).get
    var ty = data.get(id).get.types + t
    data + (id -> new CategoryTypeSchema(id,m.name,m.code,ty,m.isSystem))
    Done
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
            case SchemaCreated(schema) => Future.successful(rep.addSchema(schema))
            case TypeAdded(id,t) => Future.successful(rep.addType(id,t))
          }
        }

        )


      }
    }
  }

  override def aggregateTags = CategoryEvent.Tag.allTags
}
