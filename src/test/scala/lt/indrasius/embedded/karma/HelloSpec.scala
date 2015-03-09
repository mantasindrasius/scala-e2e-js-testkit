package lt.indrasius.embedded.karma

/**
 * Created by mantas on 15.3.5.
 */
class HelloSpec extends KarmaSpec("specs/hello.js") {
  val port = EmbeddedEnvironment.SERVER_PORT

  println(port)
}
