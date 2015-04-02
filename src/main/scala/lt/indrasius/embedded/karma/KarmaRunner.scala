package lt.indrasius.embedded.karma

import java.io.File
import java.nio.file.{Paths, Files}

/**
 * Created by mantas on 15.3.24.
 */
trait KarmaRunner extends JSSpecEnvironmentRunner {
  private val includes = Seq.newBuilder[KarmaInclude]
  private val configValues = Map.newBuilder[String, String]

  def config(values: (String, String)*) =
    configValues ++= values

  def bowerInclude(names: String*) =
    names foreach {
      includes += BowerInclude(_)
    }

  def classPathInclude(names: String*) =
    names foreach {
      includes += ClassPathInclude(_)
    }

  def fileInclude(names: String*) =
    names foreach {
      includes += FileInclude(_)
    }

  def runSpec(file: String) = {
    val configFile = File.createTempFile("jsConfig", ".js").getAbsolutePath
    val inclFiles = configFile +: (includes.result() flatMap { _.resolve })
    val content = s"var jsExecConfig = " + JSON.stringify(configValues.result()) + ";"

    Files.write(Paths.get(configFile), content.getBytes)

    inclFiles foreach { f =>
      println("Including " + f)
    }

    val karma = new EmbeddedKarma(9898, inclFiles)

    karma.startSingle(file)
  }
}

trait KarmaInclude {
  def resolve: Seq[String]
}

case class BowerInclude(name: String) extends KarmaInclude {
  def resolve: Seq[String] = Seq(JSEnv.getBowerPackage(name).mainFile)
}

case class ClassPathInclude(name: String) extends KarmaInclude {
  def resolve: Seq[String] = Option(getClass.getClassLoader.getResource(name).getFile).toSeq
}

case class FileInclude(name: String) extends KarmaInclude {
  def resolve: Seq[String] = Seq(new File(name).getAbsolutePath)
}