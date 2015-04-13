package lt.indrasius.e2e.js

import lt.indrasius.e2e.js.env.EmbeddedEnvironment

/**
 * Created by mantas on 15.3.5.
 */
class HelloKarmaSpec extends MochaSpec("specs/hello.js") with KarmaRunner {

  config("baseUrl" ->
    s"http://localhost:${EmbeddedEnvironment.SERVER_PORT}/")

  bowerInclude(
    "jquery",
    "promise-js")

  classPathInclude(
    "js-e2e/dom-parser-fix.js",
    "client.js")
}
