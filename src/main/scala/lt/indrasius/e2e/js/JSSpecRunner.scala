package lt.indrasius.e2e.js

import org.junit.runner.notification.{Failure, RunNotifier}
import org.junit.runner.{Description, Runner}

/**
 * Created by mantas on 15.3.23.
 */
class JSSpecRunner(testClass: Class[_]) extends Runner { this: JSSpecEnvironmentRunner =>
  def getDescription: Description =
    Description.createSuiteDescription(testClass)

  override def run(notifier: RunNotifier): Unit =
    // instantiate the test class
    testClass.newInstance() match {
      case spec: JSSpec =>
        spec match {
          case runner: JSSpecEnvironmentRunner =>
            run(notifier, runner, spec.specFile)
        }
      case _ => ???
    }

  def run(notifier: RunNotifier, runner: JSSpecEnvironmentRunner, specFile: String): Unit =
    runner.runSpec(specFile) foreach {
      case InfoEvent(message) =>
        println(message)
      case TestStartedEvent(description) =>
        notifier.fireTestStarted(makeDescriptionFrom(description))
      case TestFinishedEvent(description, duration) =>
        notifier.fireTestFinished(makeDescriptionFrom(description))
      case TestFailedEvent(description, error, details) =>
        val failure = details match {
          case null | "" => error
          case _ => details
        }

        notifier.fireTestFailure(failureFor(description, failure))
      case TestRunnerDisconnectedEvent(description) =>
        notifier.fireTestFailure(failureFor("a spec has timeouted", description))
      case _ =>
    }

  def failureFor(description: String, details: String) =
    new Failure(makeDescriptionFrom(description), new AssertionError(details))

  def makeDescriptionFrom(desc: String) =
    Description.createTestDescription(testClass, desc)
}
