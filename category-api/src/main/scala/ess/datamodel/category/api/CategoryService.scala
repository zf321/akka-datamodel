package ess.datamodel.category.api

import java.util.UUID

import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceAcl, ServiceCall}

trait CategoryService extends Service {
  def createSchema: ServiceCall[CategoryTypeSchema, CategoryTypeSchema]

  def getSchemas(): ServiceCall[String, Seq[CategoryTypeSchema]]

  def getSchema(id: UUID): ServiceCall[UUID, CategoryTypeSchema]

  override def descriptor: Descriptor = {
    import Service._
    named("category")
      .withCalls(
        restCall(Method.GET, "/api/category/schema", getSchemas _),
        restCall(Method.GET, "/api/category/schema/:id", getSchema _),
        restCall(Method.POST, "/api/category/schema", createSchema _)
      ).withAutoAcl(true).withAcls(
      ServiceAcl.forMethodAndPathRegex(Method.OPTIONS, "/api/category/.*")
    )
  }
}