package lt.indrasius.e2e.js.sbt

import lt.indrasius.e2e.js.{LogEvent, JSSpecEnvironmentRunner, JSSpec}
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

/**
 * Created by mantas on 15.4.12.
 */
class SbtJSSpecRunnerFactorySpec extends Specification {
  class Context extends Scope {
    val factory = new SbtJSSpecRunnerFactory
  }

  "SbtJSSpecRunnerFactory" should {
    "make a runner with JSSpec and JSSpecEnvironmentRunner class" in new Context {
      factory(classOf[TestSpecWithEnv]) must beAnInstanceOf[SbtJSSpecRunner]
    }

    "make a runner with JSSpec class" in new Context {
      factory(classOf[TestSpec]) must throwA[IllegalStateException]
    }

    "fail to make a runner with some other class" in new Context {
      factory(classOf[DummyClass]) must throwA[IllegalStateException]
    }
  }
}

class TestSpec extends JSSpec {
  def specFile = "hello.js"
}

class TestSpecWithEnv extends JSSpec with JSSpecEnvironmentRunner {
  def specFile = "hello.js"
  def runSpec(file: String): Stream[LogEvent] = ???
}

class DummyClass