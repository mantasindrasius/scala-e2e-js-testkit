package lt.indrasius.e2e.js.it

import lt.indrasius.e2e.js.env.EmbeddedEnvironment
import lt.indrasius.e2e.js.NodeMocha
import lt.indrasius.e2e.js.support.LogEventsMatchers
import org.specs2.matcher.MustMatchers
import org.specs2.mutable.SpecificationWithJUnit

/**
 * Created by mantas on 15.3.10.
 */
class NodeMochaSpec extends SpecificationWithJUnit with MustMatchers with LogEventsMatchers {
  skipAll

  val port = EmbeddedEnvironment.SERVER_PORT

  "NodeMocha" should {
    "run karma test" in {
      val result = NodeMocha.run("specs/hello-mocha.js")
      val lines = result.toIndexedSeq

      lines must containTestStartedEvent(be_===("display the greeting to the user")) and
        containTestSuiteFinishedEvent(beMatching("mocha\\.suite"))
    }
  }
}
