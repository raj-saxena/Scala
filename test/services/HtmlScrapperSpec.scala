package services

import org.jsoup.Jsoup
import org.scalatestplus.play.PlaySpec

class HtmlScrapperSpec extends PlaySpec {
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

    "return page title if present" in {
      val htmlWithPageTitle =
        """
          |<!doctype html>
          |<html>
          |<head>
          |    <title>Example Domain</title>
          |</head>
          |<body />
          |</html>
        """.stripMargin

      val pageTitle = new HtmlScrapper(Jsoup.parse(htmlWithPageTitle)).getPageTitle

      pageTitle must equal("Example Domain")
    }

    "return number of headings grouped by heading level" in {
      val htmlWithHeadings =
        """
          |<!doctype html>
          |<html>
          |<head />
          |<body>
          |<div>
          |    <h1>Heading type 1 - first</h1>
          |    <h2>Heading type 2</h2>
          |    <h6>Heading type 6</h3>
          |    <h1>Heading 1 - second</h1>
          |</div>
          |</body>
          |</html>
          |""".stripMargin

      val headings = new HtmlScrapper(Jsoup.parse(htmlWithHeadings)).getHeadingGroupedByCount

      headings("h1") must equal(2)
      headings("h2") must equal(1)
      headings("h6") must equal(1)
    }

    "return no of links grouped into internal/external" in {
      val html =
        """
          |<!doctype html>
          |<html>
          |<head />
          |<body>
          |<div>
          |   <p><a href="http://www.internal.com/internal1">More information...</a></p>
          |   <p><a href="http://internal.com/internal2">More information...</a></p>
          |   <p><a href="/internal3">More information...</a></p>
          |
          |   <p><a href="http://www.external.com/external1">More information...</a></p>
          |   <p><a href="http://subdomain.external.com/external2">More information...</a></p>
          |</div>
          |</body>
          |</html>
          |""".stripMargin

      val links = new HtmlScrapper(Jsoup.parse(html)).getLinks

      links.size must equal(5)
      links must equal(List("http://www.internal.com/internal1", "http://internal.com/internal2",
        "/internal3", "http://www.external.com/external1", "http://subdomain.external.com/external2"))
    }

    "return if form is present as 'form' element" in {
      val html =
        """
          |<!doctype html>
          |<html>
          |<head />
          |<body>
          |<div>
          |   <form action="/example">
          |     <input name="firstname" type="text"/>
          |     <input type="submit">Submit</input>
          |   </form>
          |</div>
          |</body>
          |</html>
          |""".stripMargin

      val containsForm = new HtmlScrapper(Jsoup.parse(html)).containsForm

      containsForm must equal(true)
    }

    "return if form submission is present as javascript" in {
      val html =
        """
          |<!doctype html>
          |<html>
          |<head>
          | <script>
          |   function onClickHandler() {
          |     var request = new XMLHttpRequest();
          |     request.open('POST', "https://example.com/submit");
          |     request.send("test");
          |   }
          | </script>
          |</head>
          |<body>
          |<div>
          | <input name="firstname" type="text"/>
          | <input type="submit" onClick="onClickHandler()">Submit</input>
          |</div>
          |</body>
          |</html>
          |""".stripMargin

      val containsForm = new HtmlScrapper(Jsoup.parse(html)).containsForm

      containsForm must equal(true)
    }

    "return if form submission is present as jquery ajax " in {
      val html =
        """
          |<!DOCTYPE html>
          |<html>
          |<head>
          |<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
          |<script>
          |$(document).ready(function(){
          |    $("button").click(function(){
          |        $.ajax({url: "demo_test.txt", success: function(result){
          |            $("#div1").html(result);
          |        }});
          |    });
          |});
          |</script>
          |</head>
          |<body>
          |
          |<div id="div1"><h2>Let jQuery AJAX Change This Text</h2></div>
          |
          |<button>Get External Content</button>
          |
          |</body>
          |</html>
          |""".stripMargin

      val containsForm = new HtmlScrapper(Jsoup.parse(html)).containsForm

      containsForm must equal(true)
    }
  }
}
