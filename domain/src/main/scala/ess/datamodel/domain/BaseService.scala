package ess.datamodel.domain

import akka.Done
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import com.lightbend.lagom.scaladsl.api.transport.Method

trait BaseService extends Service {
  val apiName: String

  def create(id: String): ServiceCall[String, Done]

  def get(id: String): ServiceCall[String, String]

  override def descriptor: Descriptor = {
    import Service._
    named("entity")
      .withCalls(
        restCall(Method.GET, s"/api/$apiName/:id", get _)
        //        restCall(Method.POST, "/api/$apiName/:id", create _)
      ).withAutoAcl(true)
  }
}

