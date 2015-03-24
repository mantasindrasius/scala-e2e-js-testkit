package lt.indrasius.embedded.karma

/**
 * Created by mantas on 15.3.5.
 */
class HelloKarmaSpec extends MochaSpec("specs/hello.js") with KarmaRunner {
  EmbeddedEnvironment.SERVER_PORT

  bowerInclude("jquery", "promise-js")

  fileInclude(
    "src/test/resources/dom-parser-fix.js",
    "src/test/resources/client.js")
}
