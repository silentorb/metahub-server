package silentorb.metahub.serving

import io.github.cdimascio.dotenv.dotenv
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

tailrec fun findParentDotEnvFile(path: Path = Paths.get(System.getProperty("user.dir"))): String? =
    when {
      Files.exists(path.resolve(".env")) -> path.toString()
      path.parent == null -> null
      else -> findParentDotEnvFile(path.parent)
    }

fun getDotEnvDirectory(): String =
    System.getenv("DOTENV_DIRECTORY") ?: findParentDotEnvFile() ?: ""

fun newDotEnv() = dotenv {
  directory = getDotEnvDirectory()
  ignoreIfMissing = true
}
