package lt.indrasius.e2e.js

import lt.indrasius.e2e.js.junit.JSSpecRunner
import org.junit.runner.RunWith

/**
 * Created by mantas on 15.3.11.
 */
@RunWith(classOf[JSSpecRunner])
abstract class MochaSpec(val specFile: String) extends JSSpec

trait JSSpec {
  def specFile: String
}
