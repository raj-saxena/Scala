package models

case class LinkValidResponse(isReachable: Boolean, failureReason: Option[String])
