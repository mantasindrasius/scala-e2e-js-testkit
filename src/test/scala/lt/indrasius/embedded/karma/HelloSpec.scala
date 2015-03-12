package lt.indrasius.embedded.karma

/**
 * Created by mantas on 15.3.5.
 */
class HelloSpec extends KarmaSpec("specs/hello.js") {
  EmbeddedEnvironment.SERVER_PORT
}
