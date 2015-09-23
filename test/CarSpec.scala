import controllers.Cars
import data.TestData
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner._
import play.api.mvc.Results
import play.api.test.Helpers._
import play.api.test._
import util.CustomMatchers._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import tables._
import tables.CarTable._

@RunWith(classOf[JUnitRunner])
class CarSpec extends Specification with Results with Mockito {

  "Cars" should {
    "should show all cars" in new WithApplication {
      val controller = new Cars
      Await.result(controller.dbConfig.db.run(TestData.setup), Duration.Inf)
      val request = FakeRequest(GET, "/cars")
      val result = controller.index()(request)

      status(result) must equalTo(200)
      contentType(result) must beSome.which(_ == "application/json")
      contentAsString(result) must containAllSubstringsIn(List("Audi A3", "Ferrari"))
    }

    "should show car with id 2" in new WithApplication {
      val controller = new Cars
      val request = FakeRequest(GET, "/cars/2")
      val result = controller.show(2)(request)

      status(result) must equalTo(200)
      contentType(result) must beSome.which(_ == "application/json")
      contentAsString(result) must contain("Ferrari")
      contentAsString(result) must not contain("Audi")
    }

    "should create a new record for car" in new WithApplication {
      val controller = new Cars
      Await.result(controller.dbConfig.db.run(TestData.setup), Duration.Inf)
      val request = FakeRequest(POST, "/cars").withFormUrlEncodedBody(
        "id"        -> "3",
        "title"     -> "Lada",
        "price"     -> "123",
        "is_new"    -> "true",
        "fuel"      -> "2"
      ).withHeaders(CONTENT_TYPE -> "application/x-www-form-urlencoded")
      val result = controller.create()(request)
      status(result) must equalTo(200)
      controller.findAll("id", true).map { cars =>
        cars.size must equalTo(3)
        cars.last.title must equalTo("Lada")
      }
    }

    "should show badrequest and validation errors if object wasn't saved" in new WithApplication {
      val controller = new Cars
      Await.result(controller.dbConfig.db.run(TestData.setup), Duration.Inf)
      val request = FakeRequest(POST, "/cars")
        .withHeaders(CONTENT_TYPE -> "application/x-www-form-urlencoded")

      val result = controller.create()(request)
      //badrequest is thrown
      status(result) must equalTo(400)
      contentType(result) must beSome.which(_ == "application/json")
      val errorsList = List(
        "price\":[\"This field is required\"]",
        "fuel\":[\"This field is required\"]",
        "title\":[\"This field is required\"]"
      )
      contentAsString(result) must containAllSubstringsIn(errorsList)
    }

    "should show updated record for a car with id 1" in new WithApplication {
      val controller = new Cars
      Await.result(controller.dbConfig.db.run(TestData.setup), Duration.Inf)
      val request = FakeRequest(PUT, "/cars/1")
        .withFormUrlEncodedBody(
          "id"     -> "1",
          "title"  -> "BMW i320",
          "price"  -> "123",
          "is_new" -> "true",
          "fuel"   -> "2"
        )
        .withHeaders(CONTENT_TYPE -> "application/x-www-form-urlencoded")
      val result = controller.update(1)(request)

      status(result) must equalTo(200)
      controller.findOne(1).map(_.get.title must equalTo("BMW i320"))
    }

    "should delete a car with id 1" in new WithApplication {
      val controller = new Cars
      Await.result(controller.dbConfig.db.run(TestData.setup), Duration.Inf)
      val request = FakeRequest(DELETE, "/cars/1")
      val result = controller.delete(1)(request)

      status(result) must equalTo(200)
      controller.findAll("id", false).map { cars =>
        cars.size must equalTo(1)
        cars.head.id must not equalTo(1)
      }
      contentAsString(result) must contain("deleted")
    }

    "should sort by price ascending" in new WithApplication {
      val controller = new Cars
      Await.result(controller.dbConfig.db.run(TestData.setup), Duration.Inf)
      val request = FakeRequest(GET, "/cars?sort=price&desc=0")
      val result = controller.index()(request)

      status(result) must equalTo(200)
      contentAsString(result) must startWith("[{\"id\":1")
    }

    "should sort by price descending" in new WithApplication {
      val controller = new Cars
      Await.result(controller.dbConfig.db.run(TestData.setup), Duration.Inf)
      val request = FakeRequest(GET, "/cars?sort=price&desc=1")
      val result = controller.index()(request)

      status(result) must equalTo(200)
      contentAsString(result) must startWith("[{\"id\":2")
    }

  }

}
