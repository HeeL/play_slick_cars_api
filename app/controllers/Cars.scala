package controllers

import models.Car
import tables._
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.mvc.BodyParsers._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future
import scala.concurrent.duration._
import play.api.libs.json._

class Cars extends Controller with CarTable {
  import driver.api._

  def index = Action.async { implicit request =>
    val sort = request.getQueryString("sort").getOrElse("id")
    val desc = request.getQueryString("desc").getOrElse("0") == "1"
    CarTable.findAll(sort, desc).map(res => Ok(Json.toJson(res.toList)))
  }

  def show(id: Int) = Action.async {
    CarTable.findOne(id).map(res => Ok(Json.toJson(res.get)))
  }

  val carForm = Form(
    mapping(
      "id"        -> ignored(1),
      "title"     -> nonEmptyText,
      "price"     -> number,
      "is_new"    -> boolean,
      "fuel"      -> number,
      "mileage"   -> optional(number)
    )(Car.apply)(Car.unapply)

    verifying("Mileage must be filled for used car", c => c.is_new || c.mileage.getOrElse(0) > 0)
  )

  def create = Action.async { implicit request =>
    carForm.bindFromRequest.fold(
      formWithErrors => {
        scala.concurrent.Future{BadRequest(formWithErrors.errorsAsJson)}
      },
      car => {
        CarTable.create(car).map(_ => Ok(Json.toJson(car)))
      }
    )
  }

  def update(id: Int) = Action.async { implicit request =>
    carForm.bindFromRequest.fold(
      formWithErrors => {
        scala.concurrent.Future{BadRequest(formWithErrors.errorsAsJson)}
      },
      car => {
        CarTable.update(id, car).map(_ => Ok(Json.toJson(car)))
      }
    )
  }

  def delete(id: Int) = Action.async {
    CarTable.destroy(id).map(_ => Ok(Json.toJson("deleted")))
  }

}
