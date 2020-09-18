package silentorb.metahub.database

enum class QueryExpressionType {
    all,
    expand,
    filter,
    sort
}

data class QueryExpression(
    val type: QueryExpressionType,
    val children: List<QueryExpression> = listOf(),
    val value: Any? = null
)

data class Entry(
    val source: String,
    val relationship: String,
    val target: String
)

typealias Entries = List<Entry>

typealias ExpandedEntity = Map<String, Any>

typealias ExpandedEntities = Map<String, ExpandedEntity>
