package ess.datamodel.domain.entity
import akka.Done
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait EntityService extends Service {

  def createEntity(id:String) :ServiceCall[String,Done]

  def getEntity(id:String) :ServiceCall[String,String]

  override def descriptor: Descriptor = {
    import Service._
    named("entity")
      .withCalls(
        pathCall("/api/entity/:id",getEntity _)
//        restCall(Method.POST, "/api/entity/:id", createEntity _)
      )
  }
}
