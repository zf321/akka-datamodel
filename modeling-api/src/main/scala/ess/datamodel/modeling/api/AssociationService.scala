package ess.datamodel.modeling.api

import java.util.UUID

import com.lightbend.lagom.scaladsl.api.Service._
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.Method

trait AssociationService {

  def createAssociation: ServiceCall[Association, Association]

  def getAssociations(): ServiceCall[String, Seq[Association]]

  def getAssociation(id: UUID): ServiceCall[UUID, Association]

  def associationDescriptor = Array(
    restCall(Method.GET, "/api/association", getAssociations _),
    restCall(Method.GET, "/api/association/:id", getAssociation _),
    restCall(Method.POST, "/api/association", createAssociation _))


}