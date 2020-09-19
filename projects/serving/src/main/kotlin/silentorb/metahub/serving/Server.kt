package silentorb.metahub.serving

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import silentorb.imp.core.Namespace
import silentorb.metahub.database.Database
import silentorb.metahub.database.query
import silentorb.metahub.serialization.parseJson
import silentorb.metahub.serialization.toJson

fun Application.metahubApi(namespace: Namespace, database: Database) {
  routing {
    get("/ping") {
      call.respondText("Hello World!")
    }

    post("/read") {
      val request = parseJson<ReadRequest>(call.receiveStream())
      val responses = try {
        request.queries.mapValues { query(namespace, database, it.value) }
      } catch (error: Throwable) {
        call.respond(HttpStatusCode.InternalServerError)
        throw error
      }
      val errors = responses.flatMap { it.value.errors }
      if (errors.any()) {
        call.respond(HttpStatusCode.BadRequest, toJson(errors))
      } else {
        val responseData = responses.mapValues { it.value.value }
        call.respondText(toJson(ReadResponse(data = responseData)))
      }
    }
  }
}
