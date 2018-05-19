package ess.datamodel.domain

import akka.Done
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.{PersistentEntity, PersistentEntityRegistry}

import scala.reflect.ClassTag

abstract class BaseServiceImpl[T <: PersistentEntity: ClassTag ](persistentEntityRegistry: PersistentEntityRegistry) extends BaseService {
  override def create(id: String): ServiceCall[String, Done] = ???

//  override def get(id: String) = ServiceCall { _ =>
//    val ref = persistentEntityRegistry.refFor[T](id)
//
//    ref.ask(get(id))
//  }
}

