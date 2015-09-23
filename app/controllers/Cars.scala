package controllers

import models.Car
import tables._
import play.api._
import play.api.mvc._
import play.api.Play.current
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

}
