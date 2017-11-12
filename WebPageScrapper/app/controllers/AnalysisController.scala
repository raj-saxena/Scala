package controllers

import javax.inject.{Inject, Singleton}

import forms.WebUrlInputForm
import forms.WebUrlInputForm.form
import play.api.Logger
import play.api.mvc.{AnyContent, _}
import services.PageAnalyzerService

@Singleton
class AnalysisController @Inject()(cc: MessagesControllerComponents, pageAnalyzerService: PageAnalyzerService)
  extends MessagesAbstractController(cc) {
  val logger: Logger = Logger(this.getClass)
  private val postUrl = routes.AnalysisController.analyze()

  def index = Action { implicit request: MessagesRequest[AnyContent] =>
    // Pass an unpopulated form to the template
    logger.info("Serving form")
    Ok(views.html.analyze(postUrl, WebUrlInputForm.form, None))
  }

  def analyze = Action { implicit request: MessagesRequest[AnyContent] =>
    form.bindFromRequest().fold(formWithErrors => {
      logger.error("Error in binding form")
      BadRequest(views.html.analyze(postUrl, formWithErrors, None))
    },
      urlData => {
        val url = urlData.url
        logger.info("url received =>" + url)
        val analysisResult = time(pageAnalyzerService.analyze(url), url)

        Ok(views.html.analyze(postUrl, WebUrlInputForm.form, Some(analysisResult)))
      })
  }

  def time[A](process: => A, url:String) = {
    val now = System.currentTimeMillis()
    val result = process
    val micros = (System.currentTimeMillis() - now) / 1000
    logger.info(s"Time taken to analyse $url = $micros seconds")
    result
  }
}
