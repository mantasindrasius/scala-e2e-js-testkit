package lt.indrasius.e2e.js.sbt

import lt.indrasius.e2e.js.JSSpecWithRunner

/**
 * Created by mantas on 15.4.12.
 */
class SbtJSSpecRunnerFactory {
  def apply(cls: Class[_]): SbtJSSpecRunner =
    cls.newInstance() match {
      case spec: JSSpecWithRunner =>
        new SbtJSSpecRunner(spec)
      case _ =>
        throw new IllegalStateException(s"${cls.getName} not a JSSpecWithRunner")
    }
}
