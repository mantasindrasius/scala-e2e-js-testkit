package lt.indrasius.e2e.js.common

/**
 * Created by mantas on 15.3.24.
 */
object JSON {
  import org.json4s._
  import org.json4s.native.Serialization
  import org.json4s.native.Serialization.{read, write}

  implicit private val formats = Serialization.formats(NoTypeHints)

  def parse[A](data: String)(implicit manifest: Manifest[A]): A =
    read(data)

  def stringify[A <: AnyRef](obj: A): String =
    write[A](obj)
}