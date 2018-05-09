package ess.datamodel.domain.entity

import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import ess.datamodel.domain.DomainApplication
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

class EntityServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {


  private val server = ServiceTest.startServer(
    ServiceTest.defaultSetup
      .withCassandra()
  ) { ctx =>
    new DomainApplication(ctx) with LocalServiceLocator
  }

  val client = server.serviceClient.implement[EntityService]

  override protected def afterAll() = server.stop()

  "entity service" should {

    "get entity" in {
      client.getEntity("1").invoke("1").map{
        e => e should === ("1")
      }
    }

  }

}
