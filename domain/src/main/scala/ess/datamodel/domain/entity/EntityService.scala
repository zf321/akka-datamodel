package ess.datamodel.domain.entity

import akka.Done
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait EntityService extends Service {

  def createEntity(id: String): ServiceCall[String, Done]

  def getEntity(id: String): ServiceCall[String, String]

  override def descriptor: Descriptor = {
    import Service._

    val apiName = "entity"
    named(s"$apiName")
      .withCalls(
        restCall(Method.GET, s"/api/$apiName/:id", getEntity _),
        restCall(Method.POST, s"/api/$apiName/:id", createEntity _)
      ).withAutoAcl(true)
  }
}
