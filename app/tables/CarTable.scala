package tables

import models.Car
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.Tag
import play.api.libs.json.{JsValue, Writes, Json}

object CarTable extends CarTable

trait CarTable extends HasDatabaseConfig[JdbcProfile]{
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  class Cars(tag: Tag) extends Table[Car](tag, "cars") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def title = column[String]("title")

    def price = column[Int]("price")

    def is_new = column[Boolean]("is_new")

    def fuel = column[Int]("fuel")

    def mileage = column[Option[Int]]("mileage")

    def * = (id, title, price, is_new, fuel, mileage) <> ((Car.apply _).tupled, Car.unapply)
  }

  val cars = TableQuery[Cars]

  implicit val carFormat = Json.format[Car]

  def findAll = {
    db.run(cars.result)
  }

}