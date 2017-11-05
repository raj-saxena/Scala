package services

import java.net.URI
import javax.inject.{Inject, Singleton}

import models.{AnalysisResult, LinkValidResponse}

import scala.concurrent.Await

@Singleton
class PageAnalyzerService @Inject()(htmlScrapperService: HtmlScrapperService, wsClientService: WSClientService) {

  import scala.concurrent.duration._

  def getLinkValidationResponse(links: Set[String]): Set[LinkValidResponse] = links.map(l => Await.result(wsClientService.getLinkValidResponse(l), 5.seconds))

  def analyze(url: String): AnalysisResult = {
    val domain = new URI(url).getHost
    val htmlVersion = htmlScrapperService.getHTMLVersion
    val pageTitle = htmlScrapperService.getPageTitle
    val headingGroupedByCount = htmlScrapperService.getHeadingGroupedByCount
    val links = htmlScrapperService.getLinks
    val containsForm = htmlScrapperService.containsForm
    val linkValidationResponses = getLinkValidationResponse(links)
    AnalysisResult(htmlVersion, pageTitle, headingGroupedByCount, getLinksByType(links, domain), containsForm, linkValidationResponses)
  }

  private def getLinksByType(links: Set[String], domain: String) = {
    links.groupBy {
      case e: String if e.startsWith("/") || e.contains(domain) => "internal"
      case _ => "external"
    }
  }
}
