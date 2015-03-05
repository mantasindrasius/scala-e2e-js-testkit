package lt.indrasius.embedded.karma

import org.specs2.mutable.Specification

/**
 * Created by mantas on 15.3.4.
 */
class EmbeddedKarmaIT extends Specification {
  "EmbeddedKarma" should {
    "run karma test" in {
      val karma = new EmbeddedKarma(9898)

      karma.startSingle("specs/hello.js") must beSuccessfulTry(())
    }
  }
}
