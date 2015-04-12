package lt.indrasius.e2e.js

import java.nio.file.Paths

import org.specs2.matcher.MustMatchers
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

/**
 * Created by mantas on 15.3.4.
 */
class EmbeddedKarmaIT extends Specification with MustMatchers with LogEventsMatchers {
  sequential

  class Context extends Scope {
    val relativeDeps = Seq(
      "bower_components/jquery/dist/jquery.js",
      "bower_components/promise-js/promise.js",
      "src/test/resources/dom-parser-fix.js",
      "src/test/resources/client.js")

    val deps = relativeDeps map { Paths.get(_).toAbsolutePath.toString }
  }

  "EmbeddedKarma" should {
    "run karma test" in new Context {
      val karma = new EmbeddedKarma(9898, deps)

      val result = karma.startSingle("specs/hello-karma.js")
      val lines = result.toIndexedSeq

      lines must containInfoEvent(beMatching(".*Karma\\s+.*server\\s+started.*")) and
        containTestStartedEvent(be_===("be a successful try")) and
        containBlockClosedEvent(beMatching("PhantomJS 1.9.8 \\(.*\\)"))
    }

    "get a karma disconnect message" in new Context {
      val karma = new EmbeddedKarma(9899, deps)

      val result = karma.startSingle("specs/hello-disconnect.js")
      val lines = result.toIndexedSeq

      lines must containDisconnectedEvent("Disconnected (1 times), because no message in 1000 ms.")
    }
  }
}
