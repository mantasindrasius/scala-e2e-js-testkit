package lt.indrasius.embedded.karma

import org.junit.runner.notification.{Failure, RunNotifier}
import org.junit.runner.{Description, Runner}

/**
 * Created by mantas on 15.3.11.
 */
class MochaRunner(testClass: Class[_]) extends Runner {
  def getDescription: Description =
    Description.createSuiteDescription(testClass)

  override def run(notifier: RunNotifier) = {
    val nj = new NodeJasmine()

    testClass.newInstance()

    val events = nj.run("src/test/resources/specs/hello-zombie.js")

    events foreach {
      case InfoEvent(message) =>
        println(message)
      case TestStartedEvent(description) =>
        notifier.fireTestStarted(makeDescriptionFrom(description))
      case TestFinishedEvent(description, duration) =>
        notifier.fireTestFinished(makeDescriptionFrom(description))
      case TestFailedEvent(description, message, details) =>
        notifier.fireTestFailure(failureFor(description, message))
      case TestRunnerDisconnectedEvent(description) =>
        notifier.fireTestFailure(failureFor(description, "Test run incomplete"))
      case _ =>
    }
  }

  def failureFor(description: String, message: String) =
    new Failure(makeDescriptionFrom(description), new AssertionError(message))

  def makeDescriptionFrom(desc: String) =
    Description.createTestDescription(testClass, desc)
}
