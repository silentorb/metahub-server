package silentorb.metahub.serving

import io.ktor.application.*
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
      val responseData = request.queries.mapValues { query(namespace, database, it.value) }
      call.respondText(toJson(ReadResponse(data = responseData)))
    }
  }
}
