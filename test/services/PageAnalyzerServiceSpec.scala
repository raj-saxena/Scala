package services

import models.{AnalysisResult, LinkValidResponse}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PageAnalyzerServiceSpec extends PlaySpec with MockitoSugar {
  val url = "http://www.example.com"

  "PageAnalyzerService" should {
    val htmlScrapperService = mock[HtmlScrapperService]
    val wsClientService = mock[WSClientService]

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
      val internalLinkValidationResponse = LinkValidResponse(internalLink, true, None)
      when(wsClientService.getLinkValidResponse(internalLink)) thenReturn Future(internalLinkValidationResponse)
      val externalLinkValidationResponse = LinkValidResponse(externalLink, false, Some("Internal Server Error"))
      when(wsClientService.getLinkValidResponse(externalLink)) thenReturn Future(externalLinkValidationResponse)

      val expectedAnalysisResult = AnalysisResult(Some(htmlVersion), pageTitle, headingsGroupedByCount, linksByType, containsForm,
        Set(internalLinkValidationResponse, externalLinkValidationResponse))

      val pageAnalyzerService = new PageAnalyzerService(wsClientService)
      pageAnalyzerService.htmlScrapperService = htmlScrapperService
      val analysisResult = pageAnalyzerService.analyze(url)

      analysisResult must equal(expectedAnalysisResult)
      verify(htmlScrapperService).getHTMLVersion
      verify(htmlScrapperService).getPageTitle
      verify(htmlScrapperService).getHeadingGroupedByCount
      verify(htmlScrapperService).containsForm
      verify(wsClientService).getLinkValidResponse(internalLink)
      verify(wsClientService).getLinkValidResponse(externalLink)
    }
  }
}
