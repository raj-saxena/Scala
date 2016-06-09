package com.xyz

import java.io.{File, PrintWriter}

import org.jsoup.Jsoup

import scala.collection.mutable.ArrayBuffer

object WebCrawler {

  sealed case class ReleaseItem(title: String, desc: String)

  def getTitleDescription(url: String) = {
    val doc = Jsoup.connect(url).get
    val items = doc.select("div.item") //Get div with item class

    val releaseItemList = new ArrayBuffer[ReleaseItem]()

    val it = items.iterator()
    while (it.hasNext) {
      val item = it.next();
      val title = item.select("div.title").text()
      val desc = item.select("div.desc").text()
      releaseItemList += (ReleaseItem(title, desc))
    }

    releaseItemList
  }

  def writeToFile(output: String): Unit = {
    val writer = new PrintWriter(new File("aws-release-desc.txt"))
    writer.write(output)
    writer.close()
  }

  def main(args: Array[String]): Unit = {
    val url = "http://aws.amazon.com/releasenotes/Java?c=100&p=1&sm=dD"

    val releaseItems = getTitleDescription(url)
    val output = releaseItems.map(item => item.title + " => " + item.desc).mkString("\n")
    println(output)
    writeToFile(output)
  }
}
