package silentorb.metahub.serving

data class ReadRequest(
    val queries: Map<String, String>
)
