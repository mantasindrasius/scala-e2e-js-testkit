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
    val inst = testClass.newInstance()

    val karma = new EmbeddedKarma(9898,
      Seq("bower_components/jquery/dist/jquery.js",
        "bower_components/promise-js/promise.js",
        "src/test/resources/dom-parser-fix.js",
        "src/test/resources/client.js"))

    karma.startSingle("specs/hello.js")

    val desc1 = Description.createTestDescription(testClass, "Hello world")
    val desc2 = Description.createTestDescription(testClass, "Hello world 2")

    notifier.fireTestStarted(desc1)
    notifier.fireTestRunStarted(desc1)
    //notifier.fireTestRunFinished(new Result())

    notifier.fireTestFinished(desc1)

    notifier.fireTestStarted(desc2)

    notifier.fireTestFailure(new Failure(desc2, new Exception("Fail")))
    notifier.fireTestFinished(desc2)

    notifier.pleaseStop()
  }
}
