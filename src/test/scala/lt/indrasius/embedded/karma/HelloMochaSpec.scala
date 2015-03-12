package lt.indrasius.embedded.karma

/**
 * Created by mantas on 15.3.11.
 */
class HelloMochaSpec extends MochaSpec("specs/hello-mocha.js") {
  EmbeddedEnvironment.SERVER_PORT
}
