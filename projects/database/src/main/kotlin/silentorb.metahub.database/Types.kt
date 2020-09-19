package silentorb.metahub.database

import silentorb.imp.core.PathKey
import silentorb.imp.core.newTypePair

data class Entry(
    val source: String,
    val relationship: String,
    val target: String
)

typealias Entries = List<Entry>

typealias ExpandedEntity = Map<String, Any>

typealias ExpandedEntities = Map<String, ExpandedEntity>

typealias Keys = Set<String>

typealias DatabaseToKeys = (Database) -> Keys
typealias DatabaseToObjects = (Database) -> ExpandedEntities

const val queryPath = "silentorb.metahub.querying"

val databaseToKeysType = newTypePair(PathKey(queryPath, "DatabaseToKeys"))
val databaseToObjectsType = newTypePair(PathKey(queryPath, "DatabaseToObjects"))
val databaseKeyPredicateType = newTypePair(PathKey(queryPath, "DatabaseKeyPredicate"))

typealias DatabaseKeyPredicate = (Database, String) -> Boolean
