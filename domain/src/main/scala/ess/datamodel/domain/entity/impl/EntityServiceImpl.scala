package ess.datamodel.domain.entity.impl

import akka.Done
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import ess.datamodel.domain.entity.{Entity, EntityService, queryEntity}

class EntityServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends EntityService{
  override def createEntity(id: String): ServiceCall[String, Done] = ???

  override def getEntity(id:String) = ServiceCall  { _ =>
    val ref = persistentEntityRegistry.refFor[Entity](id)

    ref.ask(queryEntity(id))
  }
}
