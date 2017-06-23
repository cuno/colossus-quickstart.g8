package $package$

import akka.util.ByteString
import colossus.core.{Initializer, ServerContext, _}
import colossus.protocols.http.HttpMethod._
import colossus.protocols.http.UrlParsing._
import colossus.protocols.http._
import colossus.protocols.memcache.Memcache.defaults._
import colossus.protocols.memcache.{Memcache, MemcacheClient}
import colossus.protocols.redis.Redis.defaults._
import colossus.protocols.redis.{Redis, RedisClient}
import colossus.service.Callback
import io.circe.Json
import io.circe.parser.parse

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RedisService(context: ServerContext, redisClient: RedisClient[Callback], cache: MemcacheClient[Callback])
  extends HttpService(context) {

  implicit def strToBs(s: String) = ByteString(s)

  implicit def bsTostr(bs: ByteString) = bs.utf8String

  implicit def jsnToBs(j: Json) = ByteString(j.toString)

  def fibonacci(x: Int): BigInt = {
    @tailrec def fib(x: Int, prev: BigInt = 0, next: BigInt = 1): BigInt = x match {
      case 0 => prev
      case 1 => next
      case _ => fib(x - 1, next, (next + prev))
    }

    fib(x)
  }

  def fibonacciFuture(x: Int) = Future(fibonacci(x))

  def handle = {
    case req@Get on Root / "fib" / Integer(n) => if (n > 0) {
      cache.get(s"fib_$"$"$n") flatMap {
        case Some(value) =>
          log.debug(s">>> cache hit")
          Callback.successful(req.ok(value.data))
        case None =>
          Callback.fromFuture(fibonacciFuture(n)) flatMap { result =>
            cache.set(s"fib_$"$"$n", ByteString(result.toString)) flatMap { set =>
              Callback.successful {
                if (!set) log.error(">>> Could not set cache")
                req.ok(result.toString)
              }
            }
          }
      }
    } else {
      Callback.successful(req.badRequest("number must be positive"))
    }

    case request@Get on Root / "hello" / name => {
      Callback.successful(request.ok(s"Hello $"$"$name!"))
    }

    case request@Get on Root / "hello" => {
      Callback.successful(request.ok("Hello world!"))
    }

    case req@Get on Root / "get" / key => {
      redisClient.getOption(key).map {
        case Some(json) =>
          parse(json) match {
            case Left(failure) => req.error(failure.message)
            case Right(json) =>
              val l = json \\\ "value"
              req.ok(l.head.toString())
          }
        case None => req.notFound("(N/A)")
      }
    }

    case req@Get on Root / "get" / key / raw => {
      redisClient.getOption(key).map {
        case Some(json) => req.ok(json.utf8String)
        case None => req.notFound("(N/A)")
      }
    }

    case req@Post on Root / "set" / key => {
      parse(req.body.toString) match {
        case Left(failure) => Callback.successful(req.error(failure.message))
        case Right(json) => redisClient.set(key, json) map { _ => req.ok("OK") }
      }
    }
  }
}

class RedisInitializer(worker: WorkerRef) extends Initializer(worker) {
  implicit val w = worker
  val redisClient = Redis.client("localhost", 6379)

  def onConnect = context => new RedisService(
    context,
    redisClient,
    Memcache.client("localhost", 11211)
  )
}

