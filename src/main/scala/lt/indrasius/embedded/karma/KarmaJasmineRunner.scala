package lt.indrasius.embedded.karma

import org.junit.runner.{Result, Description, Runner}
import org.junit.runner.notification.{Failure, RunNotifier}

/**
 * Created by mantas on 15.3.5.
 */
class KarmaJasmineRunner(testClass: Class[_]) extends Runner {
  def getDescription: Description =
    Description.createSuiteDescription(testClass)

  override def run(notifier: RunNotifier): Unit = {
    val karma = new EmbeddedKarma(9898,
      Seq("bower_components/jquery/dist/jquery.js",
        "bower_components/promise-js/promise.js",
        "src/test/resources/dom-parser-fix.js",
        "src/test/resources/client.js"))

    testClass.newInstance()

    karma.startSingle("specs/hello.js") foreach {
      case InfoEvent(message) =>
        println(message)
      case TestStartedEvent(description) =>
        notifier.fireTestStarted(makeDescriptionFrom(description))
      case TestFinishedEvent(description, duration) =>
        notifier.fireTestFinished(makeDescriptionFrom(description))
      case TestFailedEvent(description, _, details) =>
        val failure = new Failure(makeDescriptionFrom(description), new AssertionError(details))

        notifier.fireTestFailure(failure)
      case _ =>
    }
  }

  def makeDescriptionFrom(desc: String) =
    Description.createTestDescription(testClass, desc)
}
