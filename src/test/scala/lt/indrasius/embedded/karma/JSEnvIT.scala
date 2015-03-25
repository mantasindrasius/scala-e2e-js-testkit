package lt.indrasius.embedded.karma

import java.nio.file.Paths

import org.specs2.mutable.SpecificationWithJUnit

/**
 * Created by mantas on 15.3.24.
 */
class JSEnvIT extends SpecificationWithJUnit {
  skipAll

  "JSEnv" should {
    "install a node package" in {
      JSEnv.installNodePackageIfNeeded("promise")

      Paths.get(JSEnv.resolve("node_modules/promise")).toFile.exists() must beTrue
    }
  }
}