package lt.indrasius.embedded.karma

import org.specs2.matcher.Matcher
import org.specs2.mutable.Specification

/**
 * Created by mantas on 15.3.4.
 */
class EmbeddedKarmaIT extends Specification {
  val port = EmbeddedEnvironment.SERVER_PORT

  def containInfoEvent(text: String): Matcher[Seq[LogEvent]] =
    contain(beInfoEvent(be_===(text)))

  def containInfoEvent(withTextThatIs: Matcher[String]): Matcher[Seq[LogEvent]] =
    contain(beInfoEvent(withTextThatIs))

  def containTestStartedEvent(withDescriptionThatIs: Matcher[String]): Matcher[Seq[LogEvent]] =
    contain(beTestStartedEvent(withDescriptionThatIs))

  def containBlockClosedEvent(withDescriptionThatIs: Matcher[String]): Matcher[Seq[LogEvent]] =
    contain(beBlockClosedEvent(withDescriptionThatIs))

  def beInfoEvent(withTextThatIs: Matcher[String]): Matcher[LogEvent] = beLike {
    case ev: InfoEvent => ev.text must withTextThatIs
  }

  def beTestStartedEvent(withDescriptionThatIs: Matcher[String]): Matcher[LogEvent] = beLike {
    case ev: TestStartedEvent => ev.description must withDescriptionThatIs
  }

  def beBlockClosedEvent(withDescriptionThatIs: Matcher[String]): Matcher[LogEvent] = beLike {
    case ev: BlockClosedEvent => ev.description must withDescriptionThatIs
  }

  "EmbeddedKarma" should {
    "run karma test" in {
      val karma = new EmbeddedKarma(9898,
        Seq("bower_components/jquery/dist/jquery.js",
            "bower_components/promise-js/promise.js",
            "src/test/resources/dom-parser-fix.js",
            "src/test/resources/client.js"))

      val result = karma.startSingle("specs/hello.js")
      val lines = result.toIndexedSeq

      lines must containInfoEvent(beMatching(".*Karma\\s+.*server\\s+started.*")) and
        containTestStartedEvent(be_===("display the greeting to the user")) and
        containBlockClosedEvent(be_===("PhantomJS 1.9.8 (Linux)"))
    }
  }
}
