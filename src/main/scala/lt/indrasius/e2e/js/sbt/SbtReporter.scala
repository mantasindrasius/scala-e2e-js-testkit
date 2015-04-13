package lt.indrasius.e2e.js.sbt

import lt.indrasius.e2e.js._
import _root_.sbt.testing.{OptionalThrowable, TaskDef, Logger, EventHandler}

/**
 * Created by mantas on 15.4.12.
 */
class SbtReporter(taskDef: TaskDef, eventHandler: EventHandler, loggers: Array[Logger]) {
  def report(ev: LogEvent): Unit = {
    val selector = taskDef.selectors().head
    val fingerprint = taskDef.fingerprint()

    ev match {
      case TestSuiteStartedEvent(desc) =>
        eventHandler.handle(SbtPendingEvent(desc, selector, fingerprint))
      case TestSuiteFinishedEvent(desc) =>
        eventHandler.handle(SbtSuccessEvent(desc, 0, selector, fingerprint))
      case TestIgnoredEvent(desc) =>
        eventHandler.handle(SbtPendingEvent(desc, selector, fingerprint))
      case TestFinishedEvent(desc, duration) =>
        eventHandler.handle(SbtSuccessEvent(desc, duration.getOrElse(0).toLong, selector,
          fingerprint))
      case TestFailedEvent(desc, message, details) =>
        val errorMessage = s"$message $details".trim

        error(s"Failed: $desc - $errorMessage")

        eventHandler.handle(SbtFailureEvent(desc, 0, selector,
          fingerprint, new OptionalThrowable(new AssertionError(errorMessage))))
      case InfoEvent(message) =>
        info(message)
      case ev =>
        info(s"Ignored: $ev")
    }
  }

  def info(msg: String) =
    logTo { _.info(msg) }

  def error(msg: String) =
    logTo { _.error(msg) }

  def logTo(f: Logger => Unit) =
    loggers foreach { logger =>
      f(logger)
    }
}
