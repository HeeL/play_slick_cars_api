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
