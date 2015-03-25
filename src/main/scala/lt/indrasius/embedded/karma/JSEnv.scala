package lt.indrasius.embedded.karma

import java.io.File
import java.nio.file.{Files, Paths}
import org.json4s._
import org.json4s.native.JsonMethods._

import scala.sys.process.Process

/**
 * Created by mantas on 15.3.23.
 */
object JSEnv {
  private val globalDirFile = new File(".").getAbsoluteFile
  private val nodeModulesDirFile = new File(globalDirFile, "node_modules")
  private val bowerComponentsDir = new File(globalDirFile, "bower_components")

  globalDirFile.mkdirs()

  val globalDir = globalDirFile.getAbsolutePath

  def installNodePackageIfNeeded(name: String) = {
    if (!nodePackageExists(name)) {
      sys.process.Process(Seq("npm", "install", name), globalDirFile).!!
    }
  }

  def installBowerPackageIfNeeded(name: String) = {
    if (!bowerPackageExists(name)) {
      val pb = Process(s"bower install $name", globalDirFile)

      pb.run().exitValue()
    }
  }

  def nodePackageExists(name: String): Boolean =
    new File(nodeModulesDirFile, name).exists()

  def bowerPackageExists(name: String): Boolean =
    new File(bowerComponentsDir, name).exists()

  def getBowerPackage(name: String): BowerPackage = {
    installBowerPackageIfNeeded(name)

    new BowerPackage(new File(bowerComponentsDir, name).getAbsolutePath)
  }

  def resolve(name: String): String =
    globalDirFile.toPath.resolve(name).toString
}

class BowerPackage(bowerDir: String) {
  lazy val bowerJsonPath = Paths.get(bowerDir, "bower.json")
  lazy val bowerJsonContent = Files.readAllBytes(bowerJsonPath)
  lazy val bowerJson = parse(StringInput(new String(bowerJsonContent)))

  lazy val mainFile: String =
    resolve(getJsonProperties("main").head)

  def resolve(filename: String): String =
    Paths.get(bowerDir, filename).toString

  private def getJsonProperties(propName: String): Seq[String] =
    for { JObject(child) <- bowerJson
          JField(name, JString(mainFile)) <- child if name == propName }
      yield {
      mainFile
    }
}