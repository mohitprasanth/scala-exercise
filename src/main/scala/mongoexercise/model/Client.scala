package mongoexercise.model

case class Client(clientId: String, name: String, inboundFeedUrl: String, jobGroups: List[JobGroup])
