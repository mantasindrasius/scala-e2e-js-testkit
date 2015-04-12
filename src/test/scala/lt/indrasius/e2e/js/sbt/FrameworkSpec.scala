package lt.indrasius.e2e.js.sbt

import lt.indrasius.e2e.js.sbt.FrameworkSpec.TestTest
import lt.indrasius.e2e.js.{NodeRunner, MochaSpec, JSSpec}
import org.specs2.matcher.Matcher
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import sbt.testing._

import scala.collection.mutable.ListBuffer

/**
 * Created by mantas on 15.4.12.
 */
class FrameworkSpec extends Specification {
  case class TestFingerprint(isModule: Boolean,
                             requireNoArgConstructor: Boolean,
                             superclassName: String)
    extends SubclassFingerprint

  class Context extends Scope {
    val fingerprint = TestFingerprint(false, false, classOf[TestTest].getName)
    val eventHandler = new TestEventHandler
    val logger = new TestLogger
  }

  class TestEventHandler extends EventHandler {
    val collectedEvents = ListBuffer[Event]()

    def handle(ev: Event): Unit =
      collectedEvents += ev
  }

  class TestLogger extends Logger {
    val collectedInfoRecords = ListBuffer[String]()

    def ansiCodesSupported(): Boolean = false
    def warn(p1: String): Unit = ???
    def error(p1: String): Unit = ???
    def debug(p1: String): Unit = ???
    def trace(p1: Throwable): Unit = ???
    def info(message: String): Unit =
      collectedInfoRecords += message
  }

  def testSuccessEvent(name: String): Matcher[Event] =
    haveStatus(Status.Success) and haveFullyQualifiedName(name)

  def haveStatus(status: Status): Matcher[Event] =
    be_===(status) ^^ { ev: Event => ev.status() aka "status" }

  def haveFullyQualifiedName(name: String): Matcher[Event] =
    be_===(name) ^^ { ev: Event => ev.fullyQualifiedName() aka "fully qualified name" }

  "Framework" should {
    "execute a test suite" in new Context {
      val framework = new Framework
      val runner = framework.runner(Array(), Array(), getClass.getClassLoader)

      val task = new TaskDef(classOf[TestTest].getName, fingerprint, false, Array(new SuiteSelector()))

      runner.tasks(Array(task)) match {
        case Array(task) =>
          task.execute(eventHandler, Array(logger))
      }

      eventHandler.collectedEvents must contain(testSuccessEvent("should pass"))
      //logger.collectedInfoRecords must contain(be_===("XYZ"))
    }
  }
}

object FrameworkSpec {

  class TestTest extends MochaSpec("sbt/test.js") with NodeRunner

}
