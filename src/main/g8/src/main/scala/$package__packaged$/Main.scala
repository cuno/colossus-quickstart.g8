package $package$

import akka.actor.ActorSystem
import colossus.IOSystem
import colossus.core._

object Main extends App {

  implicit val actorSystem = ActorSystem()

  val name = "iosystem"
  implicit val io = IOSystem()

  Server.start("hello-redis-memcached", 9000) { worker => new RedisInitializer(worker) }
}
