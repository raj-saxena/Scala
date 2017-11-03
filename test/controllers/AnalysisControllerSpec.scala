package controllers

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test._
import play.api.test.Helpers._


class AnalysisControllerSpec extends PlaySpec with GuiceOneAppPerSuite with Injecting {
  val inputMsg = "Enter the url to analyze"
  val url = "/analyze"
  "AnalysisController GET" should  {
    "render the index page from a new instance of controller" in {
      val controller = new AnalysisController(stubControllerComponents())
      val analysePage = controller.index().apply(FakeRequest(GET, url))

      status(analysePage) mustBe OK
      contentType(analysePage) mustBe Some("text/html")
      contentAsString(analysePage) must include (inputMsg)
    }

    "render the index page from the application" in {
      val controller = inject[AnalysisController]
      val analysePage = controller.index().apply(FakeRequest(GET, url))

      status(analysePage) mustBe OK
      contentType(analysePage) mustBe Some("text/html")
      contentAsString(analysePage) must include (inputMsg)
    }

    "render the index page from the router" in {
      val request = FakeRequest(GET, url)
      val analysePage = route(app, request).get

      status(analysePage) mustBe OK
      contentType(analysePage) mustBe Some("text/html")
      contentAsString(analysePage) must include (inputMsg)
    }
  }
}
