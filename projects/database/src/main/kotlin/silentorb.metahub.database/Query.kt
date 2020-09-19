package silentorb.metahub.database

import silentorb.imp.core.*
import silentorb.imp.execution.executeToSingleValue
import silentorb.imp.parsing.parser.parseToDungeon

fun minimizeLists(it: Map.Entry<String, List<Any>>) =
    if (it.value.size == 1)
      it.value.first()
    else
      it.value

fun getKeys(triples: Entries): Keys {
  val sources = triples.map { it.source }.toSet()
  val targets = triples.map { it.target }.toSet()
  return sources + targets
}

fun expandKeys(database: Database, keys: Keys): ExpandedEntities {
  val entries = database.triples
  return keys
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

fun query(namespace: Namespace, database: Database, impCode: String): Response<ExpandedEntities> {
  // Eventually Imp should have an API for adding imports to parsing
  val imports = "import $queryPath.*\n"
  val (dungeon, errors) = parseToDungeon(namespace, imports + impCode)
  return if (errors.any())
    Response(mapOf(), errors)
  else {
    val value = executeToSingleValue(namespace, dungeon)
    if (value == null) {
      Response(mapOf(), listOf(ImpError("Error executing query")))
    } else {
      val node = getGraphOutputNode(dungeon.namespace)!!
      val type = getNodeType(listOf(dungeon.namespace), node)
      when (type) {
//        databaseToKeysType.hash -> {
////          val function = value as DatabaseToKeys
////          function(database)
//          Response(mapOf(), listOf())
//        }
        databaseToObjectsType.hash -> {
          val function = value as DatabaseToObjects
          Response(function(database), listOf())
        }
        null -> Response(mapOf(), listOf(ImpError("Unsupported output type")))
        else -> {
          val typeName = getTypeNameOrUnknown(listOf(namespace), type)
          Response(mapOf(), listOf(ImpError("Unsupported output type: $typeName")))
        }
      }
    }
  }
}
