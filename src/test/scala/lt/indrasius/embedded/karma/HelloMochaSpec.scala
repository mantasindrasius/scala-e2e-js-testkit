package lt.indrasius.embedded.karma

import lt.indrasius.embedded.karma.env.EmbeddedEnvironment

/**
 * Created by mantas on 15.3.11.
 */
class HelloMochaSpec extends MochaSpec("specs/hello-mocha.js") with NodeRunner {
  require("xmlhttprequest", "jsdom-no-contextify", "jquery")

  EmbeddedEnvironment.SERVER_PORT
}
