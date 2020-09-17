package silentorb.metahub.database

import java.io.File
import java.net.URI

data class Database(
    val triples: Entries
)

fun newDatabase(uri: URI): Database {
  val entries = loadEntries(File(uri))
  return Database(
      triples = entries
  )
}
