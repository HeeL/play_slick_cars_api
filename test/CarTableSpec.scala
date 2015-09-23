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

    "should return a list of cars" in new WithApplication {
      Await.result(CarTable.dbConfig.db.run(TestData.setup), Duration.Inf)
      val cars = Await.result(CarTable.findAll, Duration.Inf)

      cars.size must equalTo(2)
      cars.map(_.id) must equalTo(List(1,2))
    }

 }

}