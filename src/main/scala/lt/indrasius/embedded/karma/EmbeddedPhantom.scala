package lt.indrasius.embedded.karma

import java.io.File
import java.nio.file.Files

import com.twitter.io.TempDirectory

import scala.sys.process.Process
import scala.util.Try

/**
 * Created by mantas on 15.3.7.
 */
class EmbeddedPhantom {
  val tempDirectory = TempDirectory.create(true)
  def tempFile = File.createTempFile("test", ".js", tempDirectory)

  def runWith(scriptSource: String): Try[Unit] = Try {
    val scriptFile = tempFile.toPath

    Files.write(scriptFile, scriptSource.getBytes)

    Process(s"phantomjs $scriptFile").run().exitValue()
  }
}
