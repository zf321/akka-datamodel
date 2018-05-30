package ess.datamodel.modeling.api

import java.util.UUID

import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceAcl, ServiceCall}

trait ModelingService extends Service with CategoryService with AssociationService{

  //association
  def createAssociation: ServiceCall[Association, Association]

  def getAssociations(): ServiceCall[String, Seq[Association]]

  def getAssociation(id: UUID): ServiceCall[UUID, Association]

  override def descriptor: Descriptor = {
    import Service._
    named("modeling")
      .withCalls(
      ).addCalls(categoryDescriptor:_*)
      .addCalls(associationDescriptor:_*)


      .withAutoAcl(true).withAcls(
      ServiceAcl.forMethodAndPathRegex(Method.OPTIONS, "/api/.*")
    )
  }
}