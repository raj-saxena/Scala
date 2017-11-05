package services

import models.AnalysisResult
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec

class PageAnalyzerServiceSpec extends PlaySpec with MockitoSugar {

  val url = "www.example.com"

  "PageAnalyzerService" should {
    val htmlScrapperService = mock[HtmlScrapperService]

    "extract html version and page title of document" in {
      val htmlVersion = "some html version"
      when(htmlScrapperService.getHTMLVersion) thenReturn Option(htmlVersion)
      val pageTitle = "some pageTitle"
      when(htmlScrapperService.getPageTitle) thenReturn pageTitle

      val analysisResult = new PageAnalyzerService(htmlScrapperService).analyze(url)

      analysisResult must equal(AnalysisResult(Some(htmlVersion), pageTitle))
      verify(htmlScrapperService).getHTMLVersion
      verify(htmlScrapperService).getPageTitle
    }
  }
}
