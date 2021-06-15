package mongoexercise.model

case class Rule(val key:String, val operator:String, val value:List[String]){
  def checkIfApplicable(job : Job) : Boolean = {
    val cl = classOf[Job]
    val method = cl.getDeclaredMethod(key)
    val value_from_object = method.invoke(job).asInstanceOf[String]


    operator match {
      case "equal" =>
        value_from_object.equalsIgnoreCase(value.head)
      case "in" =>
        value.contains(value_from_object)
      case "begins_with" =>
        value_from_object.startsWith(value.head)
      case _ =>
        false
    }
  }
}