package lt.indrasius.e2e.js

import com.sun.media.sound.InvalidFormatException

import scala.util.{Failure, Success, Try}

/**
 * Created by mantas on 15.3.9.
 */
class TeamCityRecordParser {
  private val recordExpr = """\#\#teamcity\[([^\s]+)\s+(.*)\]""".r

  val paramsExpr = """([a-zA-Z]+)='""".r

  def parseParams(record: String): Try[TeamCityRecord] =
    record match {
      case recordExpr(name, params) =>
        Success {
          val matches = paramsExpr.findAllMatchIn(params) map { n => (n.start, n.group(1)) } toSeq

          TeamCityRecord(name, collectParams(params, matches.toList))
        }
      case _ =>
        Failure(new InvalidFormatException(s"Invalid TeamCity record structure: $record"))
    }

  def parse(record: String): Try[LogEvent] =
    parseParams(record) map {
      case TeamCityRecord("blockOpened", params) => BlockOpenedEvent(params.getOrElse("name", ""))
      case TeamCityRecord("testSuiteStarted", params) => TestSuiteStartedEvent(params.getOrElse("name", ""))
      case TeamCityRecord("testStarted", params) => TestStartedEvent(params.getOrElse("name", ""))
      case TeamCityRecord("testFinished", params) =>
        TestFinishedEvent(
          description = params.getOrElse("name", ""),
          duration = params.get("duration") flatMap {
            case "undefined" => None
            case value => Some(value.toInt)
          })
      case TeamCityRecord("testFailed", params) =>
        TestFailedEvent(
          description = params.getOrElse("name", ""),
          message = params.getOrElse("message", ""),
          details = params.getOrElse("details", ""))
      case TeamCityRecord("testSuiteFinished", params) => TestSuiteFinishedEvent(params.getOrElse("name", ""))
      case TeamCityRecord("blockClosed", params) => BlockClosedEvent(params.getOrElse("name", ""))
    }

  def collectParams(value: String, matches: List[(Int, String)]): Map[String, String] =
    matches match {
      case head :: Nil =>
        val extracted = extractValue(value, head._2, head._1, value.length)

        Map(head._2 -> extracted)
      case head :: tail =>
        val extracted = extractValue(value, head._2, head._1, tail.head._1)

        collectParams(value, tail) + (head._2 -> extracted)
    }

  private def extractValue(original: String, paramName: String, start: Int, valueEnd: Int) = {
    val valueStart = start + paramName.length + 1

    original.substring(valueStart, valueEnd).trim.stripSuffix("'").stripPrefix("'")
      .replace("|n", "\n")
      .replace("|'", "'")
  }
}

case class TeamCityRecord(name: String, params: Map[String, String])