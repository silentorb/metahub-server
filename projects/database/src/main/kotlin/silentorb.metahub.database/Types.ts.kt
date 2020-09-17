package silentorb.metahub.database

enum class QueryExpressionType {
    all
}

interface QueryExpression {
    val type: QueryExpressionType
//    val children: List<QueryExpression> = listOf()
}

data class SimpleExpression(
    override val type: QueryExpressionType
) : QueryExpression

data class Entry(
    val source: String,
    val relationship: String,
    val target: String
)

typealias Entries = List<Entry>

typealias ExpandedEntity = Map<String, Any>

typealias ExpandedEntities = Map<String, ExpandedEntity>
