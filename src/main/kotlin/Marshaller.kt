import java.io.FileReader
import java.nio.file.Path


/**
 * Marshals lines of data from the given file. It also trims each line and filters empty lines and lines that contain
 * only whitespace.
 */
fun readData(fileName: Path): List<String> {
    val fileReader = FileReader(fileName.toFile())
    return fileReader.readLines().map(String::trim).filter(String::isNotEmpty)
}