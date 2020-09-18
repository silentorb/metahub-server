package silentorb.metahub.database

data class Entry(
    val source: String,
    val relationship: String,
    val target: String
)

typealias Entries = List<Entry>

typealias ExpandedEntity = Map<String, Any>

typealias ExpandedEntities = Map<String, ExpandedEntity>

typealias QueryFunction = (Entries) -> ExpandedEntities
