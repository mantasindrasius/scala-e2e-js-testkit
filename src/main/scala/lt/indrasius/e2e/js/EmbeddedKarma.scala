package lt.indrasius.e2e.js

import java.io.File
import java.nio.file.Files

import com.twitter.io.TempDirectory

import scala.sys.process.{Process, ProcessLogger}
import scala.util.{Success, Try}

/**
 * Created by mantas on 15.3.4.
 */
class EmbeddedKarma(port: Int, dependencies: Seq[String] = Nil) {
  val tempDir = TempDirectory.create(true)
  val karmaConfig = "karma.conf.js"
  val karmaConfPath = tempDir.toPath.resolve(karmaConfig)
  val frameworks = Seq("mocha", "chai")
  val disconnectPattern = "WARN\\s\\[[^\\]]+\\]:\\s+(Disconnected\\s+\\(\\d+\\s+times\\).*)".r

  val recordParser = new TeamCityRecordParser

  def karmaConfSource(inclFiles: Seq[String]) =
    s"""
      |module.exports = function(config) {
      |  config.set({
      |    basePath: '',
      |    frameworks: [${arrayList(frameworks)}],
      |    files: [
      |      ${arrayList(inclFiles)}
      |    ],
      |    reporters: ['teamcity'],
      |    colors: false,
      |    logLevel: config.LOG_INFO,
      |    autoWatch: false,
      |    browsers: ['PhantomJS_without_security'],
      |    browserNoActivityTimeout: 1000,
      |    singleRun: false,
      |    junitReporter: {
      |      outputFile: 'test-results.xml',
      |      suite: ''
      |    },
      |    customLaunchers: {
      |      PhantomJS_without_security: {
      |        base: 'PhantomJS',
      |        flags: ['--web-security=no']
      |      }
      |    }
      |  });
      |};
    """.stripMargin

  def arrayList(items: Seq[String]) =
    items.mkString("'", "','", "'")

  def mkKarmaConfig(inclFiles: Seq[String]) = {
    val src = karmaConfSource(inclFiles)

    println(src)

    Files.write(karmaConfPath, src.getBytes)
  }

  def startSingle(filePath: String): Stream[LogEvent] = {
    val specPath = getClass.getClassLoader.getResource(filePath).toURI.getPath
    //val deps = dependencies map { new File(currDir, _).getAbsolutePath }

    JSEnv.installNodePackageIfNeeded("karma")
    JSEnv.installNodePackageIfNeeded("karma-mocha")
    JSEnv.installNodePackageIfNeeded("mocha-teamcity-reporter")
    JSEnv.installNodePackageIfNeeded("karma-chai")
    JSEnv.installNodePackageIfNeeded("karma-phantomjs-launcher")
    JSEnv.installNodePackageIfNeeded("karma-teamcity-reporter")

    mkKarmaConfig(dependencies :+ specPath)

    val logger = ProcessLogger(s => (), err => System.err.println(err))
    val process = Process(s"karma start $karmaConfPath --port=$port --single-run", new File(JSEnv.globalDir))
    val lines = process.lines_!

    process.run(logger)

    lines map { line =>
      recordParser.parse(line) match {
        case Success(event) => event
        case _ if disconnectPattern.findFirstMatchIn(line).nonEmpty =>
          val message = disconnectPattern.findFirstMatchIn(line).head.group(1)

          TestRunnerDisconnectedEvent(message)
        case _ => InfoEvent(line)
      }
    }
  }

  def start: Try[Unit] = ???
  def stop: Try[Unit] = ???
}

trait LogEvent

case class InfoEvent(text: String) extends LogEvent
case class BlockOpenedEvent(description: String) extends LogEvent
case class TestSuiteStartedEvent(description: String) extends LogEvent
case class TestStartedEvent(description: String) extends LogEvent
case class TestFinishedEvent(description: String, duration: Option[Int] = None) extends LogEvent
case class TestSuiteFinishedEvent(description: String) extends LogEvent
case class TestFailedEvent(description: String, message: String, details: String) extends LogEvent
case class BlockClosedEvent(description: String) extends LogEvent
case class TestRunnerDisconnectedEvent(description: String) extends LogEvent
