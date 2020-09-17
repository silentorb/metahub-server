package silentorb.metahub.database

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.module.kotlin.KotlinModule
import silentorb.metahub.serialization.newJsonObjectMapper

class QueryExpressionDeserializer: JsonDeserializer<QueryExpression>() {

  @Throws(IOException::class, JsonProcessingException::class)
  override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): QueryExpression {
    val node = jp.getCodec().readTree<ArrayNode>(jp)
    return SimpleExpression(QueryExpressionType.all)
  }
}

private var globalJsonMapper: ObjectMapper? = null

fun getQueryJsonObjectMapper(): ObjectMapper {
  if (globalJsonMapper == null) {
    val kotlinModule = KotlinModule()
    kotlinModule.addDeserializer(QueryExpression::class.java, QueryExpressionDeserializer())
    globalJsonMapper = newJsonObjectMapper(kotlinModule)
  }

  return globalJsonMapper!!
}
