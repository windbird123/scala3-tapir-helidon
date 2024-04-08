import io.helidon.config.Config
import io.helidon.webserver.WebServer
import sttp.tapir.*
import sttp.tapir.files.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.interceptor.cors.CORSInterceptor
import sttp.tapir.server.nima.{Id, NimaServerInterpreter, NimaServerOptions}
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC

import java.util.UUID

object NimaServer {
  final val logger: Logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    val config: Config = Config.create

    val port: Int = config.get("server.port").asInt().get
    println(port)

    import scala.jdk.CollectionConverters.*
    val tables: List[String] = config.get("database.tables").asList(classOf[String]).get().asScala.toList
    print(tables)

    ////////////////////////////////////////////////////////////////////////
    // 1. Service Endpoint 정의
    ////////////////////////////////////////////////////////////////////////
    val helloEndpoint = endpoint.get
      .in("hello")
      .out(stringBody)

    ////////////////////////////////////////////////////////////////////////
    // 2. Handler 작성
    //   - Service Endpoint 구현 => Handler
    //   - api docs Endpoints 구현 => List[Handler]
    //   - (optional) static contents Endpoint 구현 => Handler
    ////////////////////////////////////////////////////////////////////////
    val helloEndpointHandler: ServerEndpoint[Any, Id] = helloEndpoint
      .serverLogicSuccess[Id] { _ =>
        MDC.put("requestId",  UUID.randomUUID().toString.substring(0, 8))
        logger.info(s"hello endpoint was called")
        "hello, world!"
      }

    val docsHandlers: List[ServerEndpoint[Any, Id]] =
      SwaggerInterpreter().fromEndpoints[Id](List(helloEndpoint), "My App", "1.0")

    // http://localhost:8080/static/data.txt --> c:/now/data.txt
    val staticEndpointHandler: ServerEndpoint[Any, Id] = staticFilesGetServerEndpoint[Id]("static")("c:/now")

    ////////////////////////////////////////////////////////////////////////
    // 3. List[Handler] => WebServer 실행
    //    - CORS 설정
    ////////////////////////////////////////////////////////////////////////
    val customServerOptions: NimaServerOptions = NimaServerOptions.customiseInterceptors
      .corsInterceptor(CORSInterceptor.default[Id])
      .options

    val handler = NimaServerInterpreter(customServerOptions)
      .toHandler(List(helloEndpointHandler, staticEndpointHandler) ++ docsHandlers)

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
