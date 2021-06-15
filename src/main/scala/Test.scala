import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class Num(num : Int)

object Test extends App {


  val x = Future{"hai.."}
  x.isCompleted

  def extractFieldValue2[T](someObject:Any, c: Class[T], field:String) = {
    val cl = classOf[User]
    val method = cl.getDeclaredMethod("name")
    val ret = method.invoke(someObject.asInstanceOf[T])
  }

  case class User(name: String, age: Int)
  val u1 = User("user134",21)

  print(extractFieldValue2(u1,User.getClass,"name"))
}