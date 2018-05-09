package ess.datamodel.domain

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader}
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.softwaremill.macwire._
import ess.datamodel.domain.entity.impl.EntityServiceImpl
import ess.datamodel.domain.entity.{Entity, EntitySerializerRegistry, EntityService}
import play.api.libs.ws.ahc.AhcWSComponents

class DomainLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new DomainApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new DomainApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[EntityService])
}

abstract class DomainApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[EntityService](wire[EntityServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = EntitySerializerRegistry

  // Register the Hello persistent entity
  persistentEntityRegistry.register(wire[Entity])
}
