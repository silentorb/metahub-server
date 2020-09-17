import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.util.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ApiTest {

    @KtorExperimentalAPI
    @Test
    fun itWorks() = withTestApplication({ metahubApi() }) {
        with(handleRequest(HttpMethod.Get, "/ping")) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }
}
