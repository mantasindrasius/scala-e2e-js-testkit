package lt.indrasius.embedded.karma.env

import lt.indrasius.rubies.http.testkit._
import lt.indrasius.rubies.http.testkit.specs2.HttpResponseMatchers
import org.specs2.mutable.SpecificationWithJUnit

/**
 * Created by mantas on 15.3.7.
 */
class ITServerIT extends SpecificationWithJUnit with HttpResponseMatchers {
  val port = EmbeddedEnvironment.SERVER_PORT
  val client = HttpClient(s"http://localhost:$port/")

  "ITServer" should {
    "respond with a greeting" in {
      client.post("api/greeting", withBody("""{"name":"Mantas"}""")) must haveJsonBody(Greeting("Hello, Mantas"))
    }

    "respond with a HTML" in {
      client.get("greeting.html") must haveBody(contain("Hello World"))
    }
  }
}
