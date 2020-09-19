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
                "let output = all.expand"
            )
        )
    }) {
      assertEquals(HttpStatusCode.OK, response.status())
      val readResponse = parseJson<ReadResponse>(this.response.content!!)
      assertThat(readResponse.data["data"]!!.entries.size, greaterThanOrEqualTo(3))
    }
  }

  @KtorExperimentalAPI
  @Test
  fun canReadFilteredData() = withTestApplication({ metahubApi(impLibrary, testDatabase()) }) {
    with(handleRequest(HttpMethod.Post, "/read") {
        setBody(
            queryBody(
//                "let output = all.filter hasRelationship \"is\" .expand"
                "let output = expand (filter all (hasRelationship \"is\"))"
            )
        )
    }) {
      assertEquals(HttpStatusCode.OK, response.status())
      val readResponse = parseJson<ReadResponse>(this.response.content!!)
      val data = readResponse.data["data"]!!
      assertEquals(2, data.entries.size)
      assertEquals("Ogre", data["ogre"]!!["name"])
    }
  }
}
