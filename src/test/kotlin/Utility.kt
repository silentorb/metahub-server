import io.ktor.client.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.util.*

typealias ClientDelegate = (client: HttpClient) -> Unit

@KtorExperimentalAPI
fun withClient(action: ClientDelegate) {
    val env = applicationEngineEnvironment {
        module {
            metahubApi()
        }
        // Public API
        connector {
            host = "0.0.0.0"
            port = 8080
        }
    }
    val server = embeddedServer(CIO, env).start()
    Thread.sleep(2000)
    val status = HttpClient().use { client ->
        action(client)
    }
    server.stop(1000, 10000)

}
