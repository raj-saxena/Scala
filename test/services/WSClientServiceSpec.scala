package services

import models.LinkValidResponse
import org.scalatestplus.play.PlaySpec
import play.api.mvc.Results
import play.api.routing.sird._
import play.api.test._
import play.core.server.Server

import scala.concurrent.Await
import scala.concurrent.duration._


class WSClientServiceSpec extends PlaySpec {

  import scala.concurrent.ExecutionContext.Implicits.global

  "WSClientService" should {
    import Results._
    "return LinkValidResponse for success" in {
      val html =
        """
          |<!DOCTYPE HTML>
          |<html>
          | <head />
          | <body />
          |</html>
          |
        """.stripMargin

      Server.withRouterFromComponents() { components =>
        import components.{defaultActionBuilder => Action}
      {
        case GET(p"/link1") => Action {
          Ok(html)
        }
      }
      } { implicit port =>
        WsTestClient.withClient { client =>
          val result = Await.result(
            new WSClientService(client).getLinkValidResponse("/link1"), 10.seconds)
          result must equal(LinkValidResponse(true, None))
        }
      }
    }

    "return LinkValidResponse for failure" in {
      val link = "/link1"
      val errorMsg = "Internal Server Error"
      Server.withRouterFromComponents() { components =>
        import components.{defaultActionBuilder => Action}
      {
        case GET(p"/link1") => Action {
          InternalServerError(errorMsg)
        }
      }
      } { implicit port =>
        WsTestClient.withClient { client =>
          val result = Await.result(
            new WSClientService(client).getLinkValidResponse(link), 10.seconds)
          result must equal(LinkValidResponse(false, Some(errorMsg)))
        }
      }
    }
  }
}
