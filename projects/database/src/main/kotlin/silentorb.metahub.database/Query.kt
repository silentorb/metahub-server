package silentorb.metahub.database

import silentorb.imp.core.Namespace
import silentorb.imp.execution.executeToSingleValue
import silentorb.imp.parsing.parser.parseToDungeon

fun minimizeLists(it: Map.Entry<String, List<Any>>) =
    if (it.value.size == 1)
      it.value.first()
    else
      it.value

fun queryAll(triples: Entries): ExpandedEntities {
  val entries = triples
  val sources = triples.map { it.source }.distinct()
  val targets = triples.map { it.target }.distinct()
  val ids = sources + targets
  return ids
      .associateWith { id ->
        val localSources = entries
            .filter { it.source == id }
            .groupBy { it.relationship }
            .mapValues { list -> list.value.map { it.target } }
            .mapValues(::minimizeLists)

        val localTargets = entries
            .filter { it.target == id }
            .groupBy { it.relationship }
            .mapValues { list -> list.value.map { it.source } }
            .mapValues(::minimizeLists)
            .mapKeys { "@" + it.key }

        localSources + localTargets
      }
}

fun query(namespace: Namespace, database: Database, impCode: String): ExpandedEntities {
  // Eventually Imp should have an API for adding imports to parsing
  val imports = "import $queryPath.*\n"
  val (dungeon, errors) = parseToDungeon(namespace, imports + impCode)
  return if (errors.any())
    mapOf()
  else {
    val value = try {
      executeToSingleValue(namespace, dungeon)
    } catch (error: Throwable) {
      null
    }
    if (value == null) {
      mapOf()
    } else {
      val function = value as QueryFunction
      function(database.triples)
    }
  }
}
