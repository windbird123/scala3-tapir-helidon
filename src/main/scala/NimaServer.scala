import io.helidon.config.Config
import io.helidon.webserver.WebServer
import sttp.tapir.*
import sttp.tapir.files.*
import sttp.tapir.server.interceptor.cors.CORSInterceptor
import sttp.tapir.server.nima.{Id, NimaServerInterpreter, NimaServerOptions}

object NimaServer {
  def main(args: Array[String]): Unit = {
    val config: Config = Config.create

    val port: Int = config.get("server.port").asInt().get
    println(port)

    import scala.jdk.CollectionConverters.*
    val tables: List[String] = config.get("database.tables").asList(classOf[String]).get().asScala.toList
    print(tables)

    val helloEndpoint = endpoint.get
      .in("hello")
      .out(stringBody)
      .serverLogicSuccess[Id] { _ =>
        Thread.sleep(1000)
        "hello, world!"
      }

    // http://localhost:8080/docs/data.txt --> c:/now/data.txt
    val staticEndpoint = staticFilesGetServerEndpoint[Id]("docs")("c:/now")

    val customServerOptions: NimaServerOptions = NimaServerOptions.customiseInterceptors
      .corsInterceptor(CORSInterceptor.default[Id])
      .options

    val handler = NimaServerInterpreter(customServerOptions).toHandler(List(helloEndpoint, staticEndpoint))

    WebServer
      .builder()
      .config(config.get("server"))
      .routing { builder =>
        builder.any(handler)
        ()
      }
      .build()
      .start()
  }
}
