package lt.indrasius.embedded.karma

import com.sun.media.sound.InvalidFormatException

import scala.util.{Failure, Success, Try}

/**
 * Created by mantas on 15.3.9.
 */
class TeamCityRecordParser {
  private val recordExpr = """\#\#teamcity\[([^\s]+)\s+(.*)\]""".r
  private val paramsExpr = """([a-zA-Z]+)='([^']+)'""".r

  def parseParams(record: String): Try[TeamCityRecord] =
    record match {
      case recordExpr(name, params) =>
        Success {
          val mb = Map.newBuilder[String, String]

          paramsExpr.findAllMatchIn(params) foreach { m =>
            mb += m.group(1) -> m.group(2).replace("|n", "\n")
          }

          TeamCityRecord(name, mb.result())
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
          duration = params.get("duration") map { _.toInt } getOrElse(0))
      case TeamCityRecord("testFailed", params) =>
        TestFailedEvent(
          description = params.getOrElse("name", ""),
          message = params.getOrElse("message", ""),
          details = params.getOrElse("details", ""))
      case TeamCityRecord("testSuiteFinished", params) => TestSuiteFinishedEvent(params.getOrElse("name", ""))
      case TeamCityRecord("blockClosed", params) => BlockClosedEvent(params.getOrElse("name", ""))
    }
}

case class TeamCityRecord(name: String, params: Map[String, String])