package ess.datamodel.domain.category

import akka.Done
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import com.lightbend.lagom.scaladsl.api.transport.Method

trait CategoryTypeSchemaService extends Service{
  def create(id:String) :ServiceCall[String,Done]

  def get(id:String) :ServiceCall[String,String]

  override def descriptor: Descriptor = {
    import Service._
    val apiName = "categoryschema"
    named(s"$apiName")
      .withCalls(
        restCall(Method.GET,s"/api/$apiName/:id",get _)
        //        restCall(Method.POST, "/api/entity/:id", createEntity _)
      ).withAutoAcl(true)
  }
}
