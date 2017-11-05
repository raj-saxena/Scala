package services

import javax.inject.{Inject, Singleton}

import models.AnalysisResult

@Singleton
class PageAnalyzerService @Inject()(htmlScrapperService: HtmlScrapperService) {
  def analyze(url: String): AnalysisResult = {
    val htmlVersion = htmlScrapperService.getHTMLVersion
    val pageTitle = htmlScrapperService.getPageTitle
    AnalysisResult(htmlVersion, pageTitle)
  }

}
