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
      case TestStartedEvent(desc) =>
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
        log(message)
      case ev =>
        log(s"Ignored: $ev")
    }
  }

  def log(msg: String) =
    loggers foreach { logger =>
      logger.info(msg)
    }

  def error(msg: String) =
    loggers foreach { logger =>
      logger.error(msg)
    }
}
