package silentorb.metahub.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths

private var globalJsonMapper: ObjectMapper? = null

fun newJsonObjectMapper(kotlinModule: KotlinModule = KotlinModule()): ObjectMapper {
  val mapper = ObjectMapper()
  mapper.registerModule(kotlinModule)
  mapper.registerModule(getAfterburnerModule())
  mapper.enable(SerializationFeature.INDENT_OUTPUT)
  return mapper
}

fun getJsonObjectMapper(): ObjectMapper {
  if (globalJsonMapper == null) {
    globalJsonMapper = newJsonObjectMapper()
  }

  return globalJsonMapper!!
}

fun <T> saveJson(path: String, data: T) {
  Files.newBufferedWriter(Paths.get(path)).use {
    getJsonObjectMapper().writeValue(it, data)
  }
}

inline fun <reified T> parseJson(objectMapper: ObjectMapper, json: String?): T =
    objectMapper.readValue(json, T::class.java)

inline fun <reified T> parseJson(objectMapper: ObjectMapper, json: InputStream): T =
    objectMapper.readValue(json, T::class.java)

inline fun <reified T> parseJson(json: String?): T =
    getJsonObjectMapper().readValue(json, T::class.java)

inline fun <reified T> parseJson(json: InputStream): T =
    getJsonObjectMapper().readValue(json, T::class.java)

inline fun <reified T> toJson(obj: T?): String =
    getJsonObjectMapper().writeValueAsString(obj)
