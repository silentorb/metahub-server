package silentorb.metahub.serving

import silentorb.metahub.database.QueryExpression

data class ReadRequest(
    val queries: Map<String, QueryExpression>
)
