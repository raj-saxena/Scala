package controllers

import javax.inject.{Inject, Singleton}

import forms.WebUrlInputForm
import play.api.mvc.{AnyContent, _}

@Singleton
class AnalysisController @Inject()(cc: MessagesControllerComponents)
  extends MessagesAbstractController(cc) {
  private val postUrl = routes.AnalysisController.analyze()

  def index = Action { implicit request: MessagesRequest[AnyContent] =>
    // Pass an unpopulated form to the template
    Ok(views.html.analyze(postUrl, WebUrlInputForm.form, Seq()))
  }

  def analyze = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.analyze(postUrl, WebUrlInputForm.form, Seq()))
  }
}
