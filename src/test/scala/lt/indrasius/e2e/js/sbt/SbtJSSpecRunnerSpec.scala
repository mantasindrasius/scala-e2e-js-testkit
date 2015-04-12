package lt.indrasius.e2e.js.sbt

import lt.indrasius.e2e.js.{JSSpec, JSSpecEnvironmentRunner, TestSuiteStartedEvent}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

/**
 * Created by mantas on 15.4.12.
 */
class SbtJSSpecRunnerSpec extends Specification with Mockito {
  trait TestJSSpecWithRunner extends JSSpec with JSSpecEnvironmentRunner

  "SbtJSSpecRunner" should {
    "run a spec with single event" in {
      val testFile = "test.js"
      val spec = mock[TestJSSpecWithRunner]
      val reporter = mock[SbtReporter]
      val ev = TestSuiteStartedEvent("Hello")

      spec.specFile returns testFile
      spec.runSpec(testFile) returns Stream(ev)

      val runner = new SbtJSSpecRunner(spec)

      runner.run(reporter)

      got {
        one(reporter).report(ev)
      }
    }
  }
}
