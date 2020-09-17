package silentorb.metahub.database

fun minimizeLists(it: Map.Entry<String, List<Any>>) =
    if (it.value.size == 1)
      it.value.first()
    else
      it.value

fun query(database: Database, expression: QueryExpression): ExpandedEntities {
  val entries = database.triples
  val sources = database.triples.map { it.source }.distinct()
  val targets = database.triples.map { it.target }.distinct()
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
