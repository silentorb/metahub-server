import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.util.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import silentorb.metahub.database.newDatabase
import silentorb.metahub.database.newImpLibrary
import silentorb.metahub.serialization.parseJson
import silentorb.metahub.serialization.toJson
import silentorb.metahub.serving.ReadRequest
import silentorb.metahub.serving.ReadResponse
import silentorb.metahub.serving.metahubApi

fun testDatabase() = newDatabase(ApiTest::class.java.classLoader.getResource("db1")!!.toURI())

fun queryBody(value: String): String =
  toJson(
      ReadRequest(
          queries = mapOf(
              "data" to value
          )
      )
  )

val impLibrary = newImpLibrary()

class ApiTest {

  @KtorExperimentalAPI
  @Test
  fun itWorks() = withTestApplication({ metahubApi(impLibrary, testDatabase()) }) {
    with(handleRequest(HttpMethod.Get, "/ping")) {
      assertEquals(HttpStatusCode.OK, response.status())
    }
  }

  @KtorExperimentalAPI
  @Test
  fun canReadData() = withTestApplication({ metahubApi(impLibrary, testDatabase()) }) {
    with(handleRequest(HttpMethod.Post, "/read") {
        setBody(
            queryBody(
                "let output = all"
            )
        )
    }) {
      assertEquals(HttpStatusCode.OK, response.status())
      val readResponse = parseJson<ReadResponse>(this.response.content!!)
      assertThat(readResponse.data["data"]!!.entries.size, greaterThanOrEqualTo(2))
    }
  }

  @KtorExperimentalAPI
  @Test
  fun canReadFilteredData() = withTestApplication({ metahubApi(impLibrary, testDatabase()) }) {
    with(handleRequest(HttpMethod.Post, "/read") {
        setBody(
            queryBody(
                "let output = all.filter hasRelationship \"is\""
            )
        )
    }) {
      assertEquals(HttpStatusCode.OK, response.status())
      val readResponse = parseJson<ReadResponse>(this.response.content!!)
      assertThat(readResponse.data["data"]!!.entries.size, greaterThanOrEqualTo(2))
    }
  }
}
