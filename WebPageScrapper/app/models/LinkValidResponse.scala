package models

case class LinkValidResponse(url:String, isReachable: Boolean, failureReason: Option[String])
