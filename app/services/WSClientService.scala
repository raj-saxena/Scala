package services

import javax.inject.Inject

import models.LinkValidResponse
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

class WSClientService @Inject()(wsClient: WSClient)(implicit ec: ExecutionContext) {
  def getLinkValidResponse(link: String): Future[LinkValidResponse] = wsClient.url(link)
    .withFollowRedirects(true)
    .get()
    .map(r => r.status match {
      case 200 => LinkValidResponse(link, true, None)
      case _ => LinkValidResponse(link, false, Some(r.statusText))
    })
    .recover {
      case e: Exception => LinkValidResponse(link, false, Option(e.getMessage))
    }
}
