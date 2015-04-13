package lt.indrasius.e2e.js.support

import lt.indrasius.e2e.js._
import org.specs2.matcher.{Matcher, Matchers, MustMatchers}

/**
 * Created by mantas on 15.3.10.
 */
trait LogEventsMatchers { this: Matchers with MustMatchers =>

  def containInfoEvent(text: String): Matcher[Seq[LogEvent]] =
    contain(beInfoEvent(be_===(text)))

  def containInfoEvent(withTextThatIs: Matcher[String]): Matcher[Seq[LogEvent]] =
    contain(beInfoEvent(withTextThatIs))

  def containTestStartedEvent(withDescriptionThatIs: Matcher[String]): Matcher[Seq[LogEvent]] =
    contain(beTestStartedEvent(withDescriptionThatIs))

  def containBlockClosedEvent(withDescriptionThatIs: Matcher[String]): Matcher[Seq[LogEvent]] =
    contain(beBlockClosedEvent(withDescriptionThatIs))

  def containTestSuiteFinishedEvent(withDescriptionThatIs: Matcher[String]): Matcher[Seq[LogEvent]] =
    contain(beTestSuiteFinishedEvent(withDescriptionThatIs))

  def containDisconnectedEvent(description: String): Matcher[Seq[LogEvent]] =
    contain(beDisconnectedEvent(description))

  def beInfoEvent(withTextThatIs: Matcher[String]): Matcher[LogEvent] = beLike {
    case ev: InfoEvent => ev.text must withTextThatIs
  }

  def beTestStartedEvent(withDescriptionThatIs: Matcher[String]): Matcher[LogEvent] = beLike {
    case ev: TestStartedEvent => ev.description must withDescriptionThatIs
  }

  def beBlockClosedEvent(withDescriptionThatIs: Matcher[String]): Matcher[LogEvent] = beLike {
    case ev: BlockClosedEvent => ev.description must withDescriptionThatIs
  }

  def beTestSuiteFinishedEvent(withDescriptionThatIs: Matcher[String]): Matcher[LogEvent] = beLike {
    case ev: TestSuiteFinishedEvent => ev.description must withDescriptionThatIs
  }

  def beDisconnectedEvent(description: String): Matcher[LogEvent] = beLike {
    case ev: TestRunnerDisconnectedEvent => ev.description must_== description
  }
}
