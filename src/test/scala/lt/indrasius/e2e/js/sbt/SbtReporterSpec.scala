package lt.indrasius.e2e.js.sbt

import lt.indrasius.e2e.js._
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import _root_.sbt.testing._

/**
 * Created by mantas on 15.4.12.
 */
class SbtReporterSpec extends Specification with Mockito {

  val suiteSelector = new SuiteSelector()

  def testSelector(name: String) = new TestSelector(name)

  case class TestFingerprint(superclassName: String) extends SubclassFingerprint {
    lazy val isModule = false
    lazy val requireNoArgConstructor = true
  }

  class Context(selector: Selector) extends Scope {
    val eventHandler = mock[EventHandler]
    val logger = mock[Logger]
    val loggers = Array(logger)
    val suite = "Hello"
    val fingerprint = TestFingerprint("SomeClass")
    val selectors = Array(selector)
    val taskDef = new TaskDef(suite, fingerprint, false, selectors)

    val reporter = new SbtReporter(taskDef, eventHandler, loggers)

    def aSuiteEventWith(name: String, status: Status) =
      anEventWith(name, status, new SuiteSelector(), 0)

    def aTestEventWith(name: String, status: Status) =
      anEventWith(name, status, new TestSelector(name), 0)

    def aFailedTestEventWith(name: String, ex: Option[Throwable] = None) =
      anEventWith(name, Status.Failure, new TestSelector(name), 0, ex)

    def anEventWith(name: String, status: Status, selector: Selector, duration: Int, ex: Option[Throwable] = None): Event = {
      status match {
        case Status.Success => SbtSuccessEvent(name, duration, selector, fingerprint)
        case Status.Pending => SbtPendingEvent(name, selector, fingerprint)
        case Status.Failure => SbtFailureEvent(name, duration, selector, fingerprint, ex match {
          case None => new OptionalThrowable()
          case Some(ex) => new OptionalThrowable(ex)
        })
      }
    }
  }

  "SbtReporter" should {
    "report start suite" in new Context(suiteSelector) {
      reporter.report(TestSuiteStartedEvent(suite))

      got {
        one(eventHandler).handle(aSuiteEventWith(suite, Status.Pending))
      }
    }

    "report test started event" in new Context(testSelector("test 1")) {
      val test = "test 1"

      reporter.report(TestStartedEvent(test))

      got {
        one(eventHandler).handle(aTestEventWith(test, Status.Pending))
      }
    }

    "report test finished event" in new Context(testSelector("test 1")) {
      val test = "test 1"

      reporter.report(TestFinishedEvent(test))

      got {
        one(eventHandler).handle(aTestEventWith(test, Status.Success))
      }
    }

    "report test failed event" in new Context(testSelector("test 1")) {
      val test = "test 1"
      val error = new AssertionError("failed - error")

      reporter.report(TestFailedEvent(test, "failed", "error"))

      got {
        one(eventHandler).handle(any[SbtFailureEvent])
        one(logger).error("Failed: test 1 - failed error")
      }
    }

    "report suite finished" in new Context(suiteSelector) {
      reporter.report(TestSuiteFinishedEvent(suite))

      got {
        one(eventHandler).handle(aSuiteEventWith(suite, Status.Success))
      }
    }

    "log info message" in new Context(suiteSelector) {
      reporter.report(InfoEvent("Hello"))

      got {
        one(logger).info("Hello")
        noCallsTo(eventHandler)
      }
    }
  }
}
