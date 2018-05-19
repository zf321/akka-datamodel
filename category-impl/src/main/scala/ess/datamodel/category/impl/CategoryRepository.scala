package ess.datamodel.category.impl

import akka.Done
import com.datastax.driver.core.PreparedStatement
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}
import ess.datamodel.category.api.CategoryTypeSchema

import scala.concurrent.{ExecutionContext, Future}

private[impl] class CategoryRepository(session: CassandraSession)(implicit ec: ExecutionContext) {

  def getSchemas() = {
    session.selectAll("""
      SELECT * FROM CategoryTypeSchema
    """).map { rows =>
      rows.map(row => CategoryTypeSchema(Some(row.getUUID("id")),row.getString("name"),row.getString("code"),row.getBool("isSystem")))
    }
  }
}

private[impl] class CategoryEventProcessor(session: CassandraSession, readSide: CassandraReadSide)(implicit ec: ExecutionContext)
  extends ReadSideProcessor[CategoryEvent] {
  private var insertSchemaStatement: PreparedStatement = null

  override def buildHandler = {
    readSide.builder[CategoryEvent]("categoryEventOffset")
      .setGlobalPrepare(createTables)
      .setPrepare(_ => prepareStatements())
      .setEventHandler[SchemaCreated](e => insertSchema(e.event.schema))
      .build
  }

  override def aggregateTags = CategoryEvent.Tag.allTags


  private def createTables() = {
    for {
      _ <- session.executeCreateTable(
        """
        CREATE TABLE IF NOT EXISTS CategoryTypeSchema (
          id UUID PRIMARY KEY,
          name text,
          code text,
          isSystem boolean
        )
      """)
    } yield Done
  }

  private def prepareStatements() = {
    for {
      insertSchema <- session.prepare(
        """
        INSERT INTO CategoryTypeSchema(id, name,code,isSystem) VALUES (?, ?,?,?)
      """)
    } yield {
      insertSchemaStatement = insertSchema
      Done
    }
  }

  private def insertSchema(item: CategoryTypeSchema) = {
    Future.successful(List(
      insertSchemaCreator(item)
    ))
  }

  private def insertSchemaCreator(item: CategoryTypeSchema) = {
    insertSchemaStatement.bind(item.id.get, item.name,item.code,java.lang.Boolean.valueOf(item.isSystem))
  }


}
