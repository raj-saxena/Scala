package services

import javax.inject.{Inject, Singleton}

import models.AnalysisResult

@Singleton
class PageAnalyzerService @Inject()(htmlScrapperService: HtmlScrapperService) {
  def analyze(url: String): AnalysisResult =
    AnalysisResult(htmlScrapperService.getHTMLVersion)

}
