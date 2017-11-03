package services

import org.jsoup.nodes.{Document, DocumentType}
import org.jsoup.select.Elements

import scala.collection.JavaConverters

class HtmlScrapper(private val document: Document) {
  def containsForm = document.select("form").size() > 0

  def getLinks = JavaConverters.collectionAsScalaIterable(document.select("a[href]")).map(_.attr("href"))

  def getHeadingGroupedByCount = {
    val allHeadings: Elements = document.select("h1, h2, h3, h4, h5, h6")
    JavaConverters.collectionAsScalaIterable(allHeadings).groupBy(_.tagName()).map {
      case (k, v) => (k, v.size)
    }
  }

  def getPageTitle = document.title()

  def getHTMLVersion = JavaConverters.collectionAsScalaIterable(document.childNodes())
    .view.find(_.isInstanceOf[DocumentType])
    .map[String](_.attr("publicid"))
    .filterNot(_.isEmpty)
}