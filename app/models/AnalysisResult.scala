package models

case class AnalysisResult(htmlVersion: Option[String], pageTitle: String, headingsGroupedByCount: Map[String, Int],
                          linksByType: Map[String, Set[String]], containsForm: Boolean)
