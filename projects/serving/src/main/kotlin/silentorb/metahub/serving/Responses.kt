package silentorb.metahub.serving

import silentorb.metahub.database.ExpandedEntities

data class ReadResponse(
    val data: Map<String, ExpandedEntities>
)
