package ess.datamodel.modeling.impl

import java.util.UUID

import com.datastax.driver.core.utils.UUIDs
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import ess.datamodel.modeling.api.{Association, Category, ModelingService}
import ess.datamodel.modeling.impl.association.{AssociationEntity, AssociationRepository, CreateAssociation, GetAssociation}
import ess.datamodel.modeling.impl.category._

import scala.concurrent.{ExecutionContext, Future}

class ModelingServiceImpl(registry: PersistentEntityRegistry, categoryRepository: CategoryRepository, associationRepository: AssociationRepository)
                         (implicit ec: ExecutionContext) extends ModelingService {

  //category
  override def createCategory = ServerServiceCall {
    ca =>
      val id = UUIDs.timeBased()
      val sch = Category(Some(id), ca.name)
      registry.refFor[CategoryEntity](id.toString).ask(CreateCategory(sch)).map(_ => sch)

  }

  override def getCategory(id: UUID): ServiceCall[UUID, Category] = ServerServiceCall { _ =>
    registry.refFor[CategoryEntity](id.toString).ask(GetCategory).map {
      case Some(category) => category
      case None => throw NotFound("category " + id + " not found");
    }
  }

  override def getCategories(): ServiceCall[String, Seq[Category]] = ServerServiceCall {
    _ =>  Future.successful(categoryRepository.getCategorys())
  }

  override def AddChild(id: UUID): ServiceCall[Category, Category] = ServerServiceCall { ca =>
    val idd = UUIDs.timeBased()
    val sch = Category(Some(idd), ca.name)
    registry.refFor[CategoryEntity](sch.id.toString).ask(AddCategoryChild(id, sch)).map(c => c)
  }

  //association
  override def createAssociation: ServiceCall[Association, Association] = ServerServiceCall {
    ca =>
      val id = UUIDs.timeBased()
      val en = Association(Some(id), ca.from, ca.to, ca.rule, ca.fromDate, ca.endDate)
      registry.refFor[AssociationEntity](id.toString).ask(CreateAssociation(en)).map(_ => en)

  }

  override def getAssociations(): ServiceCall[String, Seq[Association]] = ServerServiceCall {
    _ => associationRepository.getAssociations()
  }

  override def getAssociation(id: UUID): ServiceCall[UUID, Association] = ServerServiceCall { _ =>
    registry.refFor[AssociationEntity](id.toString).ask(GetAssociation).map {
      case Some(association) => association
      case None => throw NotFound("category " + id + " not found");
    }
  }

}
