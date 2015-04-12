package lt.indrasius.e2e.js.sbt

import lt.indrasius.e2e.js.JSSpecWithRunner

/**
 * Created by mantas on 15.4.12.
 */
class SbtJSSpecRunner(spec: JSSpecWithRunner) {
  def run(reporter: SbtReporter): Unit = {
    spec.runSpec(spec.specFile) foreach {
      reporter.report(_)
    }
  }
}
