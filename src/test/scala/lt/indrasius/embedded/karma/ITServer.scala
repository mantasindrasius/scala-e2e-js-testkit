package lt.indrasius.embedded.karma

import lt.indrasius.rubies.http.server.EmbeddedServer
import spray.http._
import spray.http.HttpMethods._
import spray.http.StatusCodes._
import lt.indrasius.rubies.http.picking._

/**
 * Created by mantas on 15.3.7.
 */
object ITServer extends EmbeddedServer(9777) {
  val html =
    """
      |<html>
      |  <head>
      |    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
      |  </head>
      |<body>
      |  <span id="greeting">Hello World!</span>
      |</body>
      |</html>
    """.stripMargin

  override def receive = {
    case HttpRequest(POST, uri, _, entity, _) if uri.path.startsWith(Uri.Path("/api/greeting")) =>
      pick(entity) { greeter: Greeter =>
        Greeting(s"Hello, ${greeter.name}")
      }
    case HttpRequest(GET, uri, _, _, _) if uri.path.startsWith(Uri.Path("/greeting.html")) =>
      HttpResponse(OK, HttpEntity(ContentType(MediaTypes.`text/html`), html))
  }
}

case class Greeter(name: String)
case class Greeting(text: String)