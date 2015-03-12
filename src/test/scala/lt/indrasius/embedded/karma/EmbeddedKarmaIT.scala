package lt.indrasius.embedded.karma

import org.specs2.matcher.MustMatchers
import org.specs2.mutable.Specification

/**
 * Created by mantas on 15.3.4.
 */
class EmbeddedKarmaIT extends Specification with MustMatchers with LogEventsMatchers {
  val port = EmbeddedEnvironment.SERVER_PORT

  "EmbeddedKarma" should {
    "run karma test" in {
      val karma = new EmbeddedKarma(9898,
        Seq("bower_components/jquery/dist/jquery.js",
            "bower_components/promise-js/promise.js",
            "src/test/resources/dom-parser-fix.js",
            "src/test/resources/client.js"))

      val result = karma.startSingle("specs/hello.js")
      val lines = result.toIndexedSeq

      lines must containInfoEvent(beMatching(".*Karma\\s+.*server\\s+started.*")) and
        containTestStartedEvent(be_===("display the greeting to the user")) and
        containBlockClosedEvent(beMatching("PhantomJS 1.9.8 \\(.*\\)"))
    }
  }
}
