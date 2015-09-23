import data.TestData
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner._
import play.api.mvc.Results
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import tables._
import models._

class CarTableSpec extends Specification {

  "Cars" should {
    "should find car by id" in new WithApplication {
      Await.result(CarTable.dbConfig.db.run(TestData.setup), Duration.Inf)
      val car_id = 1
      val car = Await.result(CarTable.findOne(car_id), Duration.Inf)

      car.get.id must equalTo(car_id)
    }

    "should get None if no car with such id exists" in new WithApplication {
      Await.result(CarTable.dbConfig.db.run(TestData.setup), Duration.Inf)
      val car = Await.result(CarTable.findOne(9999999), Duration.Inf)

      car must equalTo(None)
    }

    "should return a list of cars" in new WithApplication {
      Await.result(CarTable.dbConfig.db.run(TestData.setup), Duration.Inf)
      val cars = Await.result(CarTable.findAll("", false), Duration.Inf)

      cars.size must equalTo(2)
      cars.map(_.id) must equalTo(List(1,2))
    }

    "should return a sorted list of cars" in new WithApplication {
      Await.result(CarTable.dbConfig.db.run(TestData.setup), Duration.Inf)
      val cars = Await.result(CarTable.findAll("title", false), Duration.Inf)

      cars.size must equalTo(2)
      cars.head.title must equalTo("Audi A3")
    }

    "should return a descending sorted list of cars" in new WithApplication {
      Await.result(CarTable.dbConfig.db.run(TestData.setup), Duration.Inf)
      val cars = Await.result(CarTable.findAll("title", true), Duration.Inf)

      cars.size must equalTo(2)
      cars.head.title must equalTo("Ferrari")
    }

    "should delete an existing car" in new WithApplication {
      Await.result(CarTable.dbConfig.db.run(TestData.setup), Duration.Inf)
      Await.result(CarTable.destroy(1), Duration.Inf)
      val cars = Await.result(CarTable.findAll("", false), Duration.Inf)

      cars.size must equalTo(1)
      cars.head.id must equalTo(2)
    }

    "should return 0 if car that was requested for deletion does not exist" in new WithApplication {
      Await.result(CarTable.dbConfig.db.run(TestData.setup), Duration.Inf)
      val result = Await.result(CarTable.destroy(9999999), Duration.Inf)
      val cars = Await.result(CarTable.findAll("", false), Duration.Inf)

      result must equalTo(0)
      cars.size must equalTo(2)
    }

 }

}