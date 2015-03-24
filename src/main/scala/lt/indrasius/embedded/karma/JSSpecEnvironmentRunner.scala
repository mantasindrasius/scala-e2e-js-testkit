package lt.indrasius.embedded.karma

/**
 * Created by mantas on 15.3.23.
 */
trait JSSpecEnvironmentRunner {
  def runSpec(file: String): Stream[LogEvent]
}
