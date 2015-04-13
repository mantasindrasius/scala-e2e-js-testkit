package lt.indrasius.e2e.js

import com.sun.media.sound.InvalidFormatException
import org.specs2.matcher.Matcher
import org.specs2.mutable.SpecificationWithJUnit

/**
 * Created by mantas on 15.3.9.
 */
class TeamCityRecordParserTest extends SpecificationWithJUnit {
  val parser = new TeamCityRecordParser

  def beTeamCityRecord(withName: String, withParamsThatAre: Matcher[Map[String, String]]): Matcher[TeamCityRecord] =
    haveName(withName) and haveParams(withParamsThatAre)

  def haveName(name: String): Matcher[TeamCityRecord] =
    be_===(name) ^^ { r: TeamCityRecord => r.name }

  def haveParams(thatAre: Matcher[Map[String, String]]): Matcher[TeamCityRecord] =
    thatAre ^^ { r: TeamCityRecord => r.params }

  "TeamCityRecordParser" should {
    "parse structure of the event with one param" in {
      parser.parseParams("##teamcity[blockOpened name='PhantomJS 1.9.8 (Linux)']") must beSuccessfulTry {
        beTeamCityRecord(
          withName = "blockOpened",
          withParamsThatAre = havePair("name" -> "PhantomJS 1.9.8 (Linux)"))
      }
    }

    "parse structure of the event with two params" in {
      parser.parseParams("##teamcity[blockOpened name='PhantomJS 1.9.8 (Linux)' something='XYZ']") must beSuccessfulTry {
        beTeamCityRecord(
          withName = "blockOpened",
          withParamsThatAre = havePairs(
            "name" -> "PhantomJS 1.9.8 (Linux)",
            "something" -> "XYZ"))
      }
    }

    "parse structure of the event with one param containing new line symbol" in {
      parser.parseParams("##teamcity[blockOpened name='line one|n line two']") must beSuccessfulTry {
        beTeamCityRecord(
          withName = "blockOpened",
          withParamsThatAre = havePair("name" -> "line one\n line two"))
      }
    }

    "parse structure of the event containing pipes" in {
      parser.parseParams("##teamcity[xyz name='respond with a greeting' message='FAILED' details='Expected |'Hello, Mantas|' to be |'XXX|'.|n    at here']") must beSuccessfulTry {
        beTeamCityRecord(
          withName = "xyz",
          withParamsThatAre = havePairs(
            "name" -> "respond with a greeting",
            "details" -> "Expected 'Hello, Mantas' to be 'XXX'.\n    at here"))
      }
    }

    "fail to parse a malformed record" in {
      parser.parseParams("xyz") must beFailedTry.withThrowable[InvalidFormatException]
    }

    "parse blockOpenedRecord" in {
      parser.parse("##teamcity[blockOpened name='PhantomJS 1.9.8 (Linux)']") must beSuccessfulTry {
        BlockOpenedEvent("PhantomJS 1.9.8 (Linux)")
      }
    }

    "parse testSuiteStarted" in {
      parser.parse("##teamcity[testSuiteStarted name='XYZ']") must beSuccessfulTry {
        TestSuiteStartedEvent("XYZ")
      }
    }

    "parse testStarted" in {
      parser.parse("##teamcity[testStarted name='DEF']") must beSuccessfulTry {
        TestStartedEvent("DEF")
      }
    }

    "parse testFinished" in {
      parser.parse("##teamcity[testFinished name='respond with a greeting' duration='78']") must beSuccessfulTry {
        TestFinishedEvent("respond with a greeting", Some(78))
      }
    }

    "parse testFinished with undefined duration" in {
      parser.parse("##teamcity[testFinished name='display the greeting to the user' duration='undefined']") must beSuccessfulTry {
        TestFinishedEvent("display the greeting to the user")
      }
    }

    "parse testFailed" in {
      parser.parse("##teamcity[testFailed name='be a failed try' message='FAILED' details='Expected true to be false.|n at there']") must beSuccessfulTry {
        TestFailedEvent("be a failed try", "FAILED", "Expected true to be false.\n at there")
      }
    }

    "parse testSuiteFinished" in {
      parser.parse("##teamcity[testSuiteFinished name='DEF']") must beSuccessfulTry {
        TestSuiteFinishedEvent("DEF")
      }
    }

    "parse testIgnored" in {
      parser.parse("##teamcity[testIgnored name='be ignored']") must beSuccessfulTry {
        TestIgnoredEvent("be ignored")
      }
    }

    "parse blockClosed" in {
      parser.parse("##teamcity[blockClosed name='DEF']") must beSuccessfulTry {
        BlockClosedEvent("DEF")
      }
    }
  }
}
