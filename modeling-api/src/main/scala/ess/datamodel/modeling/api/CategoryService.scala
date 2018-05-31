package ess.datamodel.modeling.api

import java.util.UUID

import com.lightbend.lagom.scaladsl.api.Service._
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.Method

trait CategoryService {

  //category
  def getCategories(): ServiceCall[String, Seq[Category]]

  def getCategory(id: UUID): ServiceCall[UUID, Category]

  def createCategory: ServiceCall[Category, Category]

  def AddChild(id: UUID): ServiceCall[Category, Category]

  def categoryDescriptor = Array(
    restCall(Method.GET, "/api/category", getCategories _),
    restCall(Method.GET, "/api/category/:id", getCategory _),
    restCall(Method.POST, "/api/category", createCategory _),
    restCall(Method.POST, "/api/category/:id", AddChild _)
  )


}