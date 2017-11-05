package services

import org.jsoup.nodes.{Document, DocumentType}
import org.jsoup.select.Elements

import scala.collection.JavaConverters

class HtmlScrapperService(private val document: Document) {
  private val jsPattern = """(?s)new XMLHttpRequest().*.open\(("|')POST("|')""".r
  private val jqueryPattern = """(?s)\$.ajax""".r

  def containsRequestInScript(): Boolean = {
    val script = document.getElementsByTag("script")
    val xmlHttpRequest = jsPattern.findFirstIn(script.html())

    xmlHttpRequest.isDefined
  }

  def containsJqueryRequestInScript(): Boolean = {
    val script = document.getElementsByTag("script")
    val ajaxRequest = jqueryPattern.findFirstIn(script.html())

    script.attr("src").contains("jquery") && ajaxRequest.isDefined
  }

  def containsForm: Boolean = document.select("form").size() > 0 || containsRequestInScript() || containsJqueryRequestInScript()

  def getLinks: Iterable[String] = JavaConverters.collectionAsScalaIterable(document.select("a[href]")).map(_.attr("href"))

  def getHeadingGroupedByCount: Map[String, Int] = {
    val allHeadings: Elements = document.select("h1, h2, h3, h4, h5, h6")
    JavaConverters.collectionAsScalaIterable(allHeadings).groupBy(_.tagName()).map {
      case (k, v) => (k, v.size)
    }
  }

  def getPageTitle: String = document.title()

  def getHTMLVersion: Option[String] = JavaConverters.collectionAsScalaIterable(document.childNodes())
    .view.find(_.isInstanceOf[DocumentType])
    .map[String](_.attr("publicid"))
    .filterNot(_.isEmpty)
}