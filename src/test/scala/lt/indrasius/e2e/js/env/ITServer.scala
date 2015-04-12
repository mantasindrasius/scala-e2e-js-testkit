package lt.indrasius.e2e.js.env

/// code_ref: blaze_server_example


import org.http4s.dsl._
import org.http4s.headers.`Content-Type`
import org.http4s.server._
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.MediaType._
import org.http4s.json4s.native.{jsonOf, jsonEncoderOf}
import org.json4s.{Writer, JValue, Reader}

case class Greeter(name: String)
case class Greeting(text: String)

object ITServer {
  val port = 9777
  val content =
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

  implicit val greeterDecode = new Reader[Greeter] {
    import org.json4s._

    def read(value: JValue) = {
      val JString(name) = value \ "name"

      Greeter(name)
    }
  }

  implicit val greetingEncoder = new Writer[Greeting] {
    import org.json4s.JsonDSL._

    def write(obj: Greeting): JValue =
      ("text" -> obj.text)
  }

  implicit val foDecoder = jsonOf[Greeter]
  implicit val greeting = jsonEncoderOf[Greeting]

  val service = HttpService {
    case GET -> Root / "greeting.html" =>
      Ok(content).withHeaders(`Content-Type`(`text/html`))
    case req @ POST -> Root / "api" / "greeting" =>
      val greeter = req.as[Greeter].run

      Ok().withBody(Greeting(s"Hello, ${greeter.name}")) //.withHeaders(`Content-Type`(`application/json`))
  }

  val serviceBuilder = BlazeBuilder.bindHttp(port)
    .mountService(service, "/")

  def start: Unit = serviceBuilder
    .run

  //sys.addShutdownHook(serviceBuilder)
}

object Application extends App {
  ITServer.start
}
