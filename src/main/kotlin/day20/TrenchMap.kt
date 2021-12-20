package day20

enum class Pixel {
    LIGHT,
    DARK;

    override fun toString(): String =
        when (this) {
            LIGHT -> "#"
            DARK -> "."
        }

    companion object {
        fun parse(character: Char): Pixel =
            when (character) {
                '#' -> LIGHT
                '.' -> DARK
                else -> throw IllegalStateException()
            }

        fun opposite(polarity: Pixel): Pixel =
            values().first { it != polarity }
    }
}

data class PixelCoordinate(val row: Int, val column: Int)

data class Image(val polarity: Pixel, val pixelCoordinates: Set<PixelCoordinate>) {

    fun numLitPixels(): Int =
        if (polarity == Pixel.LIGHT) {
            pixelCoordinates.size
        } else {
            Int.MAX_VALUE
        }

    fun window(row: Int, column: Int): List<Pixel> =
        (-1..1).flatMap { rowOffset -> (-1..1).map { columnOffset -> this[row + rowOffset, column + columnOffset] } }

    private operator fun get(row: Int, column: Int): Pixel =
        this[PixelCoordinate(row = row, column = column)]

    private operator fun get(pixelCoordinate: PixelCoordinate): Pixel =
        if (pixelCoordinates.contains(pixelCoordinate)) {
            polarity
        } else {
            Pixel.opposite(polarity)
        }

    override fun toString(): String {
        val firstRow = pixelCoordinates.minOf { it.row }
        val lastRow = pixelCoordinates.maxOf { it.row }
        val numRows = lastRow - firstRow + 1

        val firstColumn = pixelCoordinates.minOf { it.column }
        val lastColumn = pixelCoordinates.maxOf { it.column }
        val numColumns = lastColumn - firstColumn + 1

        val result = StringBuilder(numRows * (numColumns + 1))
        for (row in firstRow..lastRow) {
            for (column in firstColumn..lastColumn) {
                result.append(this[row, column])
            }
            result.append('\n')
        }
        return result.toString()
    }
}

fun List<Pixel>.getImageEnhancementAlgorithmIndex(): Int {
    check(this.size == 9)
    var index = 0
    for (pixel in this) {
        index = index shl 1
        if (pixel == Pixel.LIGHT) {
            index = index or 1
        }
    }
    return index
}

data class TrenchMap(val image: Image, val imageEnhancementAlgorithm: List<Pixel>) {

    fun enhance(numRounds: Int): Image {
        require(numRounds >= 1)
        var enhancedImage = image
        for (i in 1..numRounds) {
            enhancedImage = enhance(enhancedImage)
        }
        return enhancedImage
    }

    private fun enhance(toEnhance: Image): Image {
        val newPolarity =
            if (imageEnhancementAlgorithm.first() == Pixel.LIGHT && imageEnhancementAlgorithm.last() == Pixel.DARK) {
                Pixel.opposite(toEnhance.polarity)
            } else {
                toEnhance.polarity
            }
        val newPixelCoordinates = mutableSetOf<PixelCoordinate>()

        val firstRow = toEnhance.pixelCoordinates.minOf { it.row }
        val lastRow = toEnhance.pixelCoordinates.maxOf { it.row }
        val rowRange = (firstRow - 1)..(lastRow + 1)

        val firstColumn = toEnhance.pixelCoordinates.minOf { it.column }
        val lastColumn = toEnhance.pixelCoordinates.maxOf { it.column }
        val columnRange = (firstColumn - 1)..(lastColumn + 1)

        for (row in rowRange) {
            for (column in columnRange) {
                val imageEnhancementAlgorithmIndex =
                    toEnhance.window(row, column).getImageEnhancementAlgorithmIndex()
                val newPixel = imageEnhancementAlgorithm[imageEnhancementAlgorithmIndex]
                if (newPixel == newPolarity) {
                    newPixelCoordinates.add(PixelCoordinate(row = row, column = column))
                }
            }
        }
        return Image(polarity = newPolarity, pixelCoordinates = newPixelCoordinates)
    }
}

fun parseTrenchMap(input: List<String>): TrenchMap =
    TrenchMap(parseImage(input.subList(1, input.size)), parseImageEnhancementAlgorithm(input[0]))

fun parseImage(input: List<String>): Image {
    val pixels = input.flatMapIndexed { row, line ->
        line.mapIndexedNotNull { column, character ->
            if (Pixel.parse(character) == Pixel.LIGHT) {
                PixelCoordinate(row = row, column = column)
            } else {
                null
            }
        }
    }.toSet()
    return Image(polarity = Pixel.LIGHT, pixelCoordinates = pixels)
}

fun parseImageEnhancementAlgorithm(input: String): List<Pixel> =
    input.map { Pixel.parse(it) }
