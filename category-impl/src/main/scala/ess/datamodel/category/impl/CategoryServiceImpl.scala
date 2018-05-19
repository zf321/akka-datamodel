package ess.datamodel.category.impl

import java.util.UUID

import com.datastax.driver.core.utils.UUIDs
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import ess.datamodel.category.api.{CategoryService, CategoryTypeSchema}

import scala.concurrent.ExecutionContext

class CategoryServiceImpl(registry: PersistentEntityRegistry, categoryRepository: CategoryRepository)(implicit ec: ExecutionContext) extends CategoryService {
  override def createSchema = ServerServiceCall {
    schema =>
      val id = UUIDs.timeBased()
      val sch = CategoryTypeSchema(Some(id),schema.name,schema.code,schema.isSystem)
      entityRef(id).ask(CreateSchema(sch)).map(_ => sch)

  }

  override def getSchema(id: UUID): ServiceCall[UUID, CategoryTypeSchema] = ServerServiceCall { _ =>
    entityRef(id).ask(GetSchema).map {
      case Some(schema) => schema
      case None => throw NotFound("schema " + id + " not found");
    }
  }

  override def getSchemas(): ServiceCall[String, Seq[CategoryTypeSchema]] = ServerServiceCall {
    _ => categoryRepository.getSchemas()
  }


  private def entityRef(itemId: UUID) = entityRefString(itemId.toString)

  private def entityRefString(itemId: String) = registry.refFor[CategoryEntity](itemId)
}