package lt.indrasius.embedded.karma

import lt.indrasius.embedded.karma.env.EmbeddedEnvironment
import org.specs2.mutable.SpecificationWithJUnit

/**
 * Created by mantas on 15.3.7.
 */
class EmbeddedPhantomIT extends SpecificationWithJUnit {
  val port = EmbeddedEnvironment.SERVER_PORT

  "EmbeddedPhantom" should {
    "run hello world script" in {
      val embedded = new EmbeddedPhantom

      val scripts = Seq(
        "bower_components/jasmine/lib/jasmine-core/jasmine.js",
        "bower_components/jasmine/lib/jasmine-core/jasmine-html.js",
        "bower_components/jasmine/lib/jasmine-core/json2.js",
        "bower_components/jasmine/lib/jasmine-core/boot.js",
        "bower_components/jquery/dist/jquery.js")

      val jsInject = scripts.mkString("page.injectJs('", "');\r\npage.injectJs('", "');")

      val script =
        s"""
          |var page = require('webpage').create();
          |var url = 'http://localhost:$port/greeting.html';
          |
          |page.open(url, function(status) {
          |    $jsInject;
          |
          |      var elements = page.evaluate(function() {
          |          var jq = jQuery.noConflict();
          |          //return jq.fn.jquery + '; ' + $$.fn.jquery + '; ' + it;
          |          return document;
          |      });
          |
          |      phantom.exit();
          |    //});
          |});
          |
        """.stripMargin

      println(script)

      embedded.runWith(script) must beSuccessfulTry(())
    }
  }
}
