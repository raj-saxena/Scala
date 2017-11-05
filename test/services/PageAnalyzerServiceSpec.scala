package services

import models.AnalysisResult
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec

class PageAnalyzerServiceSpec extends PlaySpec with MockitoSugar {

  val url = "http://www.example.com"

  "PageAnalyzerService" should {
    val htmlScrapperService = mock[HtmlScrapperService]

    "get analysisResult for url" in {
      val htmlVersion = "some html version"
      when(htmlScrapperService.getHTMLVersion) thenReturn Option(htmlVersion)
      val pageTitle = "some pageTitle"
      when(htmlScrapperService.getPageTitle) thenReturn pageTitle
      val headingsGroupedByCount = Map("h1" -> 1, "h2" -> 6)
      when(htmlScrapperService.getHeadingGroupedByCount) thenReturn headingsGroupedByCount
      val internalLink = "www.example.com/internal"
      val externalLink = "www.external.com"
      val links = Set(internalLink, externalLink)
      val linksByType = Map("internal" -> Set(internalLink), "external" -> Set(externalLink))
      when(htmlScrapperService.getLinks) thenReturn links
      val containsForm = true
      when(htmlScrapperService.containsForm) thenReturn containsForm

      val expectedAnalysisResult = AnalysisResult(Some(htmlVersion), pageTitle, headingsGroupedByCount, linksByType, containsForm)

      val analysisResult = new PageAnalyzerService(htmlScrapperService).analyze(url)

      analysisResult must equal(expectedAnalysisResult)
      verify(htmlScrapperService).getHTMLVersion
      verify(htmlScrapperService).getPageTitle
      verify(htmlScrapperService).getHeadingGroupedByCount
      verify(htmlScrapperService).containsForm
    }
  }
}
