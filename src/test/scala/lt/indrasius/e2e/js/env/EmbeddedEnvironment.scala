package lt.indrasius.e2e.js.env

/**
 * Created by mantas on 15.3.7.
 */
object EmbeddedEnvironment {
  val SERVER_PORT = ITServer.port

  ITServer.start

  def apply() = ()
}
