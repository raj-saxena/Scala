package forms

import play.api.data.Form
import play.api.data.Forms._

object WebUrlInputForm {

  case class Data(url: String)

  val form = Form(
    mapping(
      "url" -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )

}
