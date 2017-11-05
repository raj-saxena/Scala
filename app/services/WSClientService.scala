package services

import javax.inject.{Inject, Singleton}

import models.LinkValidResponse
import play.api.Logger
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class WSClientService @Inject()(wsClient: WSClient)(implicit ec: ExecutionContext) {
  val logger = Logger(this.getClass)

  def getLinkValidResponse(link: String): Future[LinkValidResponse] = {
    logger.info(s"reaching out for '$link'")
    wsClient.url(link)
      .withFollowRedirects(true)
      .head()
      .map(r => r.status match {
        case 200 => LinkValidResponse(link, true, None)
        case _ => LinkValidResponse(link, false, Some(r.statusText))
      })
      .recover {
        case e: Exception => LinkValidResponse(link, false, Option(e.getMessage))
      }
  }
}
