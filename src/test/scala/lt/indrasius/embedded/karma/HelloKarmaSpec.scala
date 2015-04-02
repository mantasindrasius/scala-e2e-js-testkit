package lt.indrasius.embedded.karma

import lt.indrasius.embedded.karma.env.EmbeddedEnvironment

/**
 * Created by mantas on 15.3.5.
 */
class HelloKarmaSpec extends MochaSpec("specs/hello.js")
  with KarmaRunner {

  config("baseUrl" ->
    s"http://localhost:${EmbeddedEnvironment.SERVER_PORT}/")

  bowerInclude(
    "jquery",
    "promise-js")

  fileInclude(
    "src/test/resources/dom-parser-fix.js",
    "src/test/resources/client.js")
}
