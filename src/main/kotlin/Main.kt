import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.metahubApi() {
    routing {
        get("/ping") {
            call.respondText("Hello World!")
        }
    }
}
