package lt.indrasius.e2e.js

import _root_.sbt.testing._

/**
 * Created by mantas on 15.4.12.
 */
package object sbt {
  class SbtEvent(val fullyQualifiedName: String,
                 val status: Status,
                 val duration: Long,
                 val throwable: OptionalThrowable = new OptionalThrowable(),
                 val selector: Selector,
                 val fingerprint: Fingerprint)
    extends Event

  case class SbtPendingEvent(name: String,
                             override val selector: Selector,
                             override val fingerprint: Fingerprint)
    extends SbtEvent(name, Status.Pending, 0, selector = selector, fingerprint = fingerprint)

  case class SbtSuccessEvent(name: String,
                             override val duration: Long,
                             override val selector: Selector,
                             override val fingerprint: Fingerprint)
    extends SbtEvent(name, Status.Success, duration, selector = selector, fingerprint = fingerprint)

  case class SbtFailureEvent(name: String,
                             override val duration: Long,
                             override val selector: Selector,
                             override val fingerprint: Fingerprint,
                             override val throwable: OptionalThrowable)
    extends SbtEvent(name, Status.Failure, duration, selector = selector, fingerprint = fingerprint)
}
