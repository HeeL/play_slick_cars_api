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

  def findAll(sortBy: String, desc: Boolean) = {
    db.run(cars.sortBy(sortField(_, sortBy, desc)).result)
  }

  def findOne(id: Int) = db.run(find(id).result.headOption)

  def destroy(id: Int) = db.run(find(id).delete)

  private def find(id: Int) = cars.filter(_.id === id)

  private def sortField(car: Cars, sortBy: String, desc: Boolean) = {
    (sortBy, desc) match {
      case ("title",   false) => car.title.asc
      case ("title",   true)  => car.title.desc
      case ("price",   false) => car.price.asc
      case ("price",   true)  => car.price.desc
      case ("is_new",  false) => car.is_new.asc
      case ("is_new",  true)  => car.is_new.desc
      case ("mileage", false) => car.mileage.asc
      case ("mileage", true)  => car.mileage.desc
      case ("id",      true)  => car.id.desc
      case _                  => car.id.asc
    }
  }

}