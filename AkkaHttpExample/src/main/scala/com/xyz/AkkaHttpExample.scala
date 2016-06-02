package com.xyz

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{getFromResource, path}
import akka.stream._

trait SampleApp {
  implicit val system = ActorSystem("akka-http-sample")
  sys.addShutdownHook({
    system.terminate()
  })

  implicit val materializer = ActorMaterializer()

  val route =
    path("hello") {
      getFromResource("web/index.html");
    }

  val serverBinding = Http(system).bindAndHandle(route, interface = "localhost", port = 8080)
}

object Main extends App with SampleApp {

}