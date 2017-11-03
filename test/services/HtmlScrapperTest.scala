package services

import org.jsoup.Jsoup
import org.scalatestplus.play.PlaySpec

class HtmlScrapperTest extends PlaySpec {
  "HTML scrapper service" should {
    "return html version empty if present" in {
      val html =
        """
          |<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
          |<html>
          | <head />
          | <body />
          |</html>
          |
        """.stripMargin
      val htmlVersion = new HtmlScrapper(Jsoup.parse(html)).getHTMLVersion

      htmlVersion must equal(Some("-//W3C//DTD HTML 4.01//EN"))
    }

    "return html version empty if not present" in {
      val html =
        """
          |<!DOCTYPE HTML>
          |<html>
          | <head />
          | <body />
          |</html>
          |
        """.stripMargin
      val htmlVersion = new HtmlScrapper(Jsoup.parse(html)).getHTMLVersion

      htmlVersion must equal(None)
    }
  }
}
