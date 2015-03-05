package lt.indrasius.embedded.karma

import java.nio.file.Files

import com.twitter.io.{TempDirectory, TempFile}

import scala.sys.process.Process
import scala.util.Try

/**
 * Created by mantas on 15.3.4.
 */
class EmbeddedKarma(port: Int) {
  val tempDir = TempDirectory.create(true)
  val karmaConfig = "karma.conf.js"
  val karmaConfPath = tempDir.toPath.resolve(karmaConfig)

  def karmaConfSource(inclFiles: Seq[String]) =
    s"""
      |module.exports = function(config) {
      |  config.set({
      |    basePath: '',
      |    frameworks: ['jasmine'],
      |    files: [
      |      ${inclFiles.mkString("'", ",", "'")}
      |    ],
      |    reporters: ['progress'],
      |    colors: false,
      |    logLevel: config.LOG_INFO,
      |    autoWatch: false,
      |    browsers: ['PhantomJS'],
      |    browserNoActivityTimeout: 1000,
      |    singleRun: false
      |  });
      |};
    """.stripMargin

  def mkKarmaConfig(inclFiles: Seq[String]) = {
    val src = karmaConfSource(inclFiles)

    println(src)

    Files.write(karmaConfPath, src.getBytes)
  }

  def startSingle(filePath: String): Try[Unit] = Try {
    val specPath = getClass.getClassLoader.getResource(filePath).toURI.getPath

    mkKarmaConfig(Seq(specPath))

    Process(s"karma start $karmaConfPath --port=$port --single-run").run().exitValue()
  }

  def start: Try[Unit] = ???
  def stop: Try[Unit] = ???
}
