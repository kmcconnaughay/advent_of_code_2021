import java.io.FileReader
import java.nio.file.Path
import java.nio.file.Paths

private const val projectDirectory = "C:\\Users\\Allie\\IdeaProjects\\advent_of_code_2021"
private const val kotlinDirectory = "src\\main\\kotlin"

/**
 * Marshals lines of data from the given file. It also trims each line and filters empty lines and lines that contain
 * only whitespace.
 */
fun readData(day: Int, removeBlankLines: Boolean = true): List<String> {
    val fileReader = FileReader(fileName(day).toFile())
    val lines = fileReader.readLines().map { it.trim() }

    return if (removeBlankLines) {
        lines.filter { it.isNotEmpty() }
    } else {
        lines
    }
}

private fun fileName(day: Int): Path {
    val dayDirectory = "day${day.toString().padStart(length = 2, padChar = '0')}"
    return Paths.get(projectDirectory, kotlinDirectory, dayDirectory, "input.txt")
}