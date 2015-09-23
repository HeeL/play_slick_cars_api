import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class IntegrationSpec extends Specification {
  "Cars" should {

    "show list of cars from within a browser" in new WithBrowser {
      browser.goTo("http://localhost:" + port + "/cars")

      browser.pageSource must contain("Ferrari")
      browser.pageSource must contain("Audi A3")
    }

  }
}
