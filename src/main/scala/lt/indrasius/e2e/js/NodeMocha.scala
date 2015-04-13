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

  def makeRunnerScript(testFile: String) =
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

  def run(filePath: String): Stream[LogEvent] = {
    val globalDirFile = new File(JSEnv.globalDir)

    val runnerFile = File.createTempFile("run", ".js", globalDirFile)
    val runScript = makeRunnerScript(filePath)

    runnerFile.deleteOnExit()

    Files.write(runnerFile.toPath, runScript.getBytes)

    val process = Process(s"node $runnerFile", globalDirFile)

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
