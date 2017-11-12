package controllers

import models.AnalysisResult
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.mvc.MessagesControllerComponents
import play.api.test.Helpers._
import play.api.test._
import services.PageAnalyzerService

class AnalysisControllerSpec extends PlaySpec with GuiceOneAppPerSuite with Injecting with MockitoSugar {
  private val pageAnalyzerService = mock[PageAnalyzerService]
  val inputMsg = "Enter the url to analyze"
  val url = "/analyze"
  "AnalysisController GET" should {
    "render the index page from a new instance of controller" in {
      val messagesControllerComponents = inject[MessagesControllerComponents]
      val controller = new AnalysisController(messagesControllerComponents, pageAnalyzerService)
      val analysePage = controller.index().apply(FakeRequest(GET, url))

      status(analysePage) mustBe OK
      contentType(analysePage) mustBe Some("text/html")
      contentAsString(analysePage) must include(inputMsg)
    }

    "render the index page from the application" in {
      val controller = inject[AnalysisController]
      val analysePage = controller.index().apply(FakeRequest(GET, url))

      status(analysePage) mustBe OK
      contentType(analysePage) mustBe Some("text/html")
      contentAsString(analysePage) must include(inputMsg)
    }

    "render the index page from the router" in {
      val request = FakeRequest(GET, url)
      val analysePage = route(app, request).get

      status(analysePage) mustBe OK
      contentType(analysePage) mustBe Some("text/html")
      contentAsString(analysePage) must include(inputMsg)
    }

    "call pageAnalyzerService" in {
      val messagesControllerComponents = inject[MessagesControllerComponents]
      val controller = new AnalysisController(messagesControllerComponents, pageAnalyzerService)
      val urlToAnalyze = "http://www.example.com"
      when(pageAnalyzerService.analyze(urlToAnalyze)) thenReturn AnalysisResult(Some(""), "", Map(), Map(), false, Set())
      val analysePage = controller.analyze.apply(FakeRequest(POST, url)
        .withFormUrlEncodedBody("url" -> urlToAnalyze))

      status(analysePage) mustBe OK
      contentType(analysePage) mustBe Some("text/html")
      verify(pageAnalyzerService).analyze(urlToAnalyze)
    }
  }
}
