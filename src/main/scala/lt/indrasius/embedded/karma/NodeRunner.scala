package lt.indrasius.embedded.karma

/**
 * Created by mantas on 15.3.24.
 */
trait NodeRunner extends JSSpecEnvironmentRunner {
  def require(names: String*) =
    names foreach { JSEnv.installNodePackageIfNeeded(_) }

  def runSpec(file: String): Stream[LogEvent] = {
    val nj = new NodeJasmine

    nj.run(getClass.getClassLoader.getResource(file).getFile)
  }
}
