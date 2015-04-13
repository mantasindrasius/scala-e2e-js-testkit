package lt.indrasius.e2e.js.it

import java.nio.file.Paths

import lt.indrasius.e2e.js.JSEnv
import org.specs2.mutable.SpecificationWithJUnit

/**
 * Created by mantas on 15.3.24.
 */
class JSEnvSpec extends SpecificationWithJUnit {
  skipAll

  "JSEnv" should {
    "install a node package" in {
      JSEnv.installNodePackageIfNeeded("promise")

      Paths.get(JSEnv.resolve("node_modules/promise")).toFile.exists() must beTrue
    }
  }
}
