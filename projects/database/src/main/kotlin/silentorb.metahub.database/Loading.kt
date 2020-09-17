package silentorb.metahub.database

import silentorb.metahub.serialization.parseYaml
import java.io.File

val frontMatterPattern = Regex("""\s*---\s*\n(.*?)\n---\s*(.*)""", RegexOption.DOT_MATCHES_ALL)
val whitespacePattern = Regex("""^\s*$""")

fun entriesFromMarkdownString(id: String, text: String?): Entries =
    if (text == null || text.matches(whitespacePattern))
      listOf()
    else
      listOf(
          Entry(id, "content", text)
      )

fun loadEntriesFromMarkdownFile(file: File): Entries {
  val content = file.readText()
  val match = frontMatterPattern.find(content)
  return if (match != null) {
    val yaml = match.groups[1]?.value
    val markdown = match.groups[2]?.value
    val id = file.nameWithoutExtension
    val yamlEntries = if (yaml != null)
      parseYaml<Map<String, Any>>(yaml).flatMap { (key, value) ->
        when (value) {
          is String -> listOf(Entry(id, key, value))
          is Collection<*> -> value.map { Entry(id, key, it as? String ?: throw Error("String value required in YAML front matter list ($key: $value)")) }
          else -> throw Error("Invalid YAML front matter value ($key: $value)")
        }
      }
    else
      listOf()

    yamlEntries + entriesFromMarkdownString(id, markdown)
  } else {
    listOf()
  }
}

fun loadEntries(file: File): Entries {
  return if (file.isDirectory)
    file.listFiles()?.flatMap(::loadEntries) ?: listOf()
  else
    loadEntriesFromMarkdownFile(file)
}
