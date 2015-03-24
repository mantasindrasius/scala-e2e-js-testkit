package lt.indrasius.embedded.karma

import org.specs2.matcher.MustMatchers
import org.specs2.mutable.SpecificationWithJUnit

/**
 * Created by mantas on 15.3.10.
 */
class NodeJasmineIT extends SpecificationWithJUnit with MustMatchers with LogEventsMatchers {
  skipAll

  val port = EmbeddedEnvironment.SERVER_PORT

  "EmbeddedKarma" should {
    "run karma test" in {
      val nj = new NodeJasmine()

      /*Seq("bower_components/jquery/dist/jquery.js",
          "bower_components/promise-js/promise.js",
          "src/test/resources/dom-parser-fix.js",
          "src/test/resources/client.js")*/

      val result = nj.run("src/test/resources/specs/hello-mocha.js")
      val lines = result.toIndexedSeq

      lines must containTestStartedEvent(be_===("display the greeting to the user")) and
        containTestSuiteFinishedEvent(beMatching("mocha\\.suite"))
    }
  }
}
