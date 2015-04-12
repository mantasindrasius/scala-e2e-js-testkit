package lt.indrasius.e2e.js

import java.io.File
import java.nio.file.Files

import scala.sys.process.{Process, ProcessLogger}
import scala.util.Success

/**
 * Created by mantas on 15.3.10.
 */
class NodeMocha {
  val recordParser = new TeamCityRecordParser

  def makeRunnerScript(testFile:String) =
    s"""
      |var Mocha = require('mocha'),
      |    fs = require('fs'),
      |    path = require('path');
      |
      |// First, you need to instantiate a Mocha instance.
      |var mocha = new Mocha({
      |  reporter: 'mocha-teamcity-reporter'
      |});
      |
      |// Then, you need to use the method "addFile" on the mocha
      |// object for each file.
      |mocha.addFile('$testFile');
      |
      |// Now, you can run the tests.
      |mocha.run(function(failures){
      |  process.on('exit', function () {
      |    process.exit(failures);
      |  });
      |});
    """.stripMargin

  def makePackageJson =
    """
      |{
      |    "name": "mocha-test",
      |    "version": "1.0.0",
      |    "devDependencies": {
      |        "mocha": "*",
      |        "xmlhttprequest": "*",
      |        "jsdom": "*",
      |        "contextify": "*",
      |        "mocha-teamcity-reporter": "*"
      |    },
      |    "engines": {
      |        "node": ">=0.8.0"
      |    }
      |}
      |
    """.stripMargin

  def run(filePath: String): Stream[LogEvent] = {
    val tempDir = new File(".")
    val tempPath = tempDir.toPath

    val runnerFile = tempPath.resolve("run.js")

    Files.write(runnerFile, makeRunnerScript(filePath).getBytes)

    val process = Process(s"node $runnerFile")

    val logger = ProcessLogger(s => (), err => ())
    val lines = process.lines_!

    process.run(logger)

    lines map { line =>
      recordParser.parse(line) match {
        case Success(event) => event
        case _ => InfoEvent(line)
      }
    }
  }
}