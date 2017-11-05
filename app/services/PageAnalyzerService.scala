package services

import java.net.URI
import javax.inject.{Inject, Singleton}

import models.AnalysisResult

@Singleton
class PageAnalyzerService @Inject()(htmlScrapperService: HtmlScrapperService) {
  def analyze(url: String): AnalysisResult = {
    val domain = new URI(url).getHost
    val htmlVersion = htmlScrapperService.getHTMLVersion
    val pageTitle = htmlScrapperService.getPageTitle
    val headingGroupedByCount = htmlScrapperService.getHeadingGroupedByCount
    val containsForm = htmlScrapperService.containsForm
    AnalysisResult(htmlVersion, pageTitle, headingGroupedByCount, getLinksByType(domain), containsForm)
  }

  private def getLinksByType(domain: String) = {
    val links = htmlScrapperService.getLinks
    links.groupBy {
      case e: String if e.startsWith("/") || e.contains(domain) => "internal"
      case _ => "external"
    }
  }
}
