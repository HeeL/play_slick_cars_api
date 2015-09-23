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

  def index = Action.async {
    CarTable.findAll.map(res => Ok(Json.toJson(res.toList)))
  }

}
