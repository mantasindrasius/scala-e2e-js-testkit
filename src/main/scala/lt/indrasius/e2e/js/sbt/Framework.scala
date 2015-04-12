package lt.indrasius.e2e.js.sbt

import lt.indrasius.e2e.js.MochaSpec
import sbt.testing.{Event => SbtEvent, Framework => SbtFramework, Runner => SbtRunner, Status => SbtStatus, _}

/**
 * Created by mantas on 15.4.12.
 */
class Framework extends SbtFramework {
  val specRunnerFactory = new SbtJSSpecRunnerFactory

  def name(): String = "js-e2e"

  def fingerprints(): Array[Fingerprint] = Array(new SubclassFingerprint {
    def requireNoArgConstructor(): Boolean = false
    def isModule: Boolean = false
    def superclassName(): String = classOf[MochaSpec].getName
  })

  def runner(args: Array[String], remoteArgs: Array[String], testClassLoader: ClassLoader): SbtRunner =
    new Runner(args, remoteArgs, testClassLoader, specRunnerFactory)
}

class Runner(val args: Array[String], val remoteArgs: Array[String], testClassLoader: ClassLoader,
             specRunnerFactory: SbtJSSpecRunnerFactory) extends SbtRunner {

  def tasks(tasks: Array[TaskDef]): Array[Task] =
    tasks map { t =>
      val specRunner = specRunnerFactory(testClassLoader.loadClass(t.fullyQualifiedName()))
      new TaskRunner(t, testClassLoader, specRunner)
    }

  def done(): String = ""
}

class TaskRunner(val taskDef: TaskDef,
                 testClassLoader: ClassLoader,
                 specRunner: SbtJSSpecRunner) extends Task {

  def tags(): Array[String] = Array()
  def execute(eventHandler: EventHandler, loggers: Array[Logger]): Array[Task] = {
    specRunner.run(new SbtReporter(taskDef, eventHandler, loggers))

    Array()
  }
}
