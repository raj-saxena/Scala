package services

import org.jsoup.nodes.{Document, DocumentType}

import scala.collection.JavaConverters

class HtmlScrapper(private val document: Document) {
  def getHTMLVersion = JavaConverters.collectionAsScalaIterable(document.childNodes())
    .view.find(_.isInstanceOf[DocumentType])
    .map[String](_.attr("publicid"))
    .filterNot(_.isEmpty)
}