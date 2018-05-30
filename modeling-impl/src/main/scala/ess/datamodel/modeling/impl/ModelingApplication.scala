package ess.datamodel.modeling.impl

import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.rp.servicediscovery.lagom.scaladsl.LagomServiceLocatorComponents
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import ess.datamodel.modeling.api.ModelingService
import ess.datamodel.modeling.impl.category.{CategoryEntity, CategoryEventProcessor, CategoryRepository, CategorySerializerRegistry}
import play.api.Environment
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc.EssentialFilter
import play.filters.cors.CORSComponents

import scala.concurrent.ExecutionContext

trait ModelingComponents extends LagomServerComponents
  with CassandraPersistenceComponents {

  implicit def executionContext: ExecutionContext
  def environment: Environment

  override lazy val lagomServer = serverFor[ModelingService](wire[ModelingServiceImpl])
  lazy val categoryRepository = wire[CategoryRepository]
  lazy val jsonSerializerRegistry = CategorySerializerRegistry

  persistentEntityRegistry.register(wire[CategoryEntity])
  readSide.register(wire[CategoryEventProcessor])
}

abstract class ModelingApplication(context: LagomApplicationContext) extends LagomApplication(context)
  with ModelingComponents
  with AhcWSComponents
  with LagomKafkaComponents
  with CORSComponents {

  override val httpFilters: Seq[EssentialFilter] = Seq(corsFilter)
  lazy val modelingService = serviceClient.implement[ModelingService]

}

class ModelingApplicationLoader extends LagomApplicationLoader {
  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new ModelingApplication(context) with LagomDevModeComponents

  override def load(context: LagomApplicationContext): LagomApplication =
    new ModelingApplication(context) with LagomServiceLocatorComponents

  override def describeService = Some(readDescriptor[ModelingService])
}
