package lt.indrasius.embedded.karma

import java.io.File

/**
 * Created by mantas on 15.3.24.
 */
trait KarmaRunner extends JSSpecEnvironmentRunner {
  private val includes = Seq.newBuilder[KarmaInclude]

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
    val inclFiles = includes.result() flatMap { _.resolve }

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
  def resolve: Seq[String] = Seq(getClass.getClassLoader.getResource(name).getFile)
}

case class FileInclude(name: String) extends KarmaInclude {
  def resolve: Seq[String] = Seq(new File(name).getAbsolutePath)
}