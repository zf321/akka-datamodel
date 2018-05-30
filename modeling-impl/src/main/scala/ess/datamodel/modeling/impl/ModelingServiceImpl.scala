package ess.datamodel.modeling.impl

import java.util.UUID

import com.datastax.driver.core.utils.UUIDs
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import ess.datamodel.modeling.api.{Association, Category, ModelingService}
import ess.datamodel.modeling.impl.category._

import scala.concurrent.ExecutionContext

class ModelingServiceImpl(registry: PersistentEntityRegistry, categoryRepository: CategoryRepository)(implicit ec: ExecutionContext) extends ModelingService {

  //category
  override def createCategory = ServerServiceCall {
    ca =>
      val id = UUIDs.timeBased()
      val sch = Category(Some(id),ca.name)
      registry.refFor[CategoryEntity](id.toString).ask(CreateCategory(sch)).map(_ => sch)

  }

  override def getCategory(id: UUID): ServiceCall[UUID, Category] = ServerServiceCall { _ =>
    registry.refFor[CategoryEntity](id.toString).ask(GetCategory).map {
      case Some(category) => category
      case None => throw NotFound("category " + id + " not found");
    }
  }

  override def getCategories(): ServiceCall[String, Seq[Category]] = ServerServiceCall {
    _ => categoryRepository.getCategorys()
  }

  //association
  override def createAssociation: ServiceCall[Association, Association] = ???

  override def getAssociations(): ServiceCall[String, Seq[Association]] = ???

  override def getAssociation(id: UUID): ServiceCall[UUID, Association] = ???
}
