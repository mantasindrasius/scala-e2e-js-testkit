package lt.indrasius.e2e.js

import java.io.File
import java.nio.file.{Files, Paths}

import org.json4s._
import org.json4s.native.JsonMethods._

import scala.sys.process.Process

/**
 * Created by mantas on 15.3.23.
 */
object JSEnv {
  private val userHome = sys.props.get("user.home").getOrElse(".")
  private val userHomeFile = new File(userHome)
  private val globalDirFile = new File(userHomeFile, ".js-e2e").getAbsoluteFile
  private val nodeModulesDirFile = new File(globalDirFile, "node_modules")
  private val bowerComponentsDir = new File(globalDirFile, "bower_components")
  private val npmLock = new AnyRef
  private val bowerLock = new AnyRef

  globalDirFile.mkdirs()

  val globalDir = globalDirFile.getAbsolutePath

  def installNodePackageIfNeeded(name: String) = {
    if (!nodePackageExists(name)) {
      npmLock.synchronized {
        //sys.process.Process(Seq("npm", "install", name), globalDirFile).lineStream.toIndexedSeq
        val pb = Process(s"npm install $name", globalDirFile)

        pb.run().exitValue()
      }
    }
  }

  def installBowerPackageIfNeeded(name: String) = {
    if (!bowerPackageExists(name)) {
      bowerLock.synchronized {
        val pb = Process(s"bower install $name", globalDirFile)

        pb.run().exitValue()
      }
    }
  }

  def getNodePackageDir(name: String): String =
    new File(nodeModulesDirFile, name).getAbsolutePath

  def nodePackageExists(name: String): Boolean =
    new File(getNodePackageDir(name)).exists()

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