package forms

import play.api.data.Form
import play.api.data.Forms._

object WebUrlInputForm {

  case class Data(url: String)
  private val urlPattern = "^https?:\\/\\/[www.]?[a-zA-Z0-9]+.[a-zA-Z0-9]*.[a-z]{3}/?.*".r

  def validate(url: String):Option[Data] = url match {
    case urlPattern() => Some(Data(url))
    case _ => None
  }

  val form = Form(
    mapping(
      "url" -> nonEmptyText
    )(Data.apply)(Data.unapply) verifying("invalid url", _ match {
      case data => validate(data.url).isDefined
    })
  )
}
