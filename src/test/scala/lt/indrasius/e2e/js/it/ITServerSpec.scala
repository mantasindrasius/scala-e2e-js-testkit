package lt.indrasius.e2e.js.it

import lt.indrasius.e2e.js.env.EmbeddedEnvironment
import lt.indrasius.e2e.js.{KarmaRunner, MochaSpec}

/**
 * Created by mantas on 15.3.5.
 */
class ITServerSpec extends MochaSpec("specs/hello.js") with KarmaRunner {

  config("baseUrl" ->
    s"http://localhost:${EmbeddedEnvironment.SERVER_PORT}/")

  bowerInclude(
    "jquery",
    "promise-js")

  classPathInclude(
    "js-e2e/dom-parser-fix.js",
    "client.js")
}
