package day16

fun sumOfVersionNumbers(packet: Packet): Long =
    packet.header.version.number + packet.expression.subPackets.sumOf { sumOfVersionNumbers(it) }

data class ParseResult<T>(val value: T, val endIndex: Int)

data class Packet(val header: Header, val expression: Expression) {
    companion object {
        fun parse(binary: String, startIndex: Int = 0): ParseResult<Packet> {
            val header = Header.parse(binary, startIndex)
            val expression = Expression.parse(binary, header.endIndex, header.value.typeId)
            val packet = Packet(header.value, expression.value)
            return ParseResult(packet, expression.endIndex)
        }
    }
}

data class Header(val version: Version, val typeId: TypeId) {
    companion object {
        fun parse(binary: String, startIndex: Int): ParseResult<Header> {
            val version = Version.parse(binary, startIndex)
            val typeId = TypeId.parse(binary, version.endIndex)
            val header = Header(version.value, typeId.value)
            return ParseResult(header, typeId.endIndex)
        }
    }
}

data class Version(val number: Int) {
    companion object {
        fun parse(binary: String, startIndex: Int): ParseResult<Version> {
            val endIndex = startIndex + 3
            val version = Version(binary.substring(startIndex, endIndex).toInt(radix = 2))
            return ParseResult(version, endIndex)
        }
    }
}

enum class TypeId {
    SUM,
    PRODUCT,
    MINIMUM,
    MAXIMUM,
    LITERAL,
    GREATER_THAN,
    LESS_THAN,
    EQUAL_TO;

    companion object {
        fun parse(binary: String, startIndex: Int): ParseResult<TypeId> {
            val endIndex = startIndex + 3
            return when (binary.substring(startIndex, endIndex).toInt(radix = 2)) {
                0 -> ParseResult(SUM, endIndex)
                1 -> ParseResult(PRODUCT, endIndex)
                2 -> ParseResult(MINIMUM, endIndex)
                3 -> ParseResult(MAXIMUM, endIndex)
                4 -> ParseResult(LITERAL, endIndex)
                5 -> ParseResult(GREATER_THAN, endIndex)
                6 -> ParseResult(LESS_THAN, endIndex)
                7 -> ParseResult(EQUAL_TO, endIndex)
                else -> throw IllegalStateException()
            }
        }
    }
}

sealed class Expression(open val subPackets: List<Packet>) {

    abstract fun computeValue(): Long

    companion object {
        fun parse(binary: String, startIndex: Int, typeId: TypeId): ParseResult<Expression> {
            return when (typeId) {
                TypeId.SUM -> Sum.parse(binary, startIndex)
                TypeId.PRODUCT -> Product.parse(binary, startIndex)
                TypeId.MINIMUM -> Minimum.parse(binary, startIndex)
                TypeId.MAXIMUM -> Maximum.parse(binary, startIndex)
                TypeId.LITERAL -> Literal.parse(binary, startIndex)
                TypeId.GREATER_THAN -> GreaterThan.parse(binary, startIndex)
                TypeId.LESS_THAN -> LessThan.parse(binary, startIndex)
                TypeId.EQUAL_TO -> EqualTo.parse(binary, startIndex)
            }
        }

        fun parseSubPackets(binary: String, startIndex: Int): ParseResult<List<Packet>> {
            val lengthTypeId = LengthTypeId.parse(binary, startIndex)
            val subPackets = when (lengthTypeId.value) {
                LengthTypeId.TOTAL_LENGTH -> parseTotalLength(binary, lengthTypeId.endIndex)
                LengthTypeId.NUMBER_OF_SUB_PACKETS -> parseNumberOfSubPackets(binary, lengthTypeId.endIndex)
            }
            return ParseResult(subPackets.value, subPackets.endIndex)
        }

        private fun parseTotalLength(binary: String, startIndex: Int): ParseResult<List<Packet>> {
            var endIndex = startIndex + 15
            val totalLength = binary.substring(startIndex, endIndex).toInt(radix = 2)
            val subPackets = mutableListOf<Packet>()
            while (endIndex < startIndex + 15 + totalLength) {
                val subPacket = Packet.parse(binary, endIndex)
                subPackets.add(subPacket.value)
                endIndex = subPacket.endIndex
            }
            return ParseResult(subPackets, endIndex)
        }

        private fun parseNumberOfSubPackets(binary: String, startIndex: Int): ParseResult<List<Packet>> {
            var endIndex = startIndex + 11
            val numberOfSubPackets = binary.substring(startIndex, endIndex).toInt(radix = 2)
            val subPackets = mutableListOf<Packet>()
            for (i in 1..numberOfSubPackets) {
                val subPacket = Packet.parse(binary, endIndex)
                subPackets.add(subPacket.value)
                endIndex = subPacket.endIndex
            }
            return ParseResult(subPackets, endIndex)
        }
    }
}

data class Literal(val value: Long) : Expression(listOf()) {

    override fun computeValue(): Long {
        return value
    }

    companion object {
        fun parse(binary: String, startIndex: Int): ParseResult<Expression> {
            var endIndex = startIndex
            var literalBits = ""
            do {
                val continueParsing = binary[endIndex] == '1'
                literalBits += binary.substring(endIndex + 1, endIndex + 5)
                endIndex += 5
            } while (continueParsing)
            return ParseResult(Literal(literalBits.toLong(radix = 2)), endIndex)
        }
    }
}

data class Sum(override val subPackets: List<Packet>) : Expression(subPackets) {

    override fun computeValue(): Long {
        return subPackets.sumOf { it.expression.computeValue() }
    }

    companion object {
        fun parse(binary: String, startIndex: Int): ParseResult<Expression> {
            val (subPackets, endIndex) = parseSubPackets(binary, startIndex)
            return ParseResult(Sum(subPackets), endIndex)
        }
    }
}

data class Product(override val subPackets: List<Packet>) : Expression(subPackets) {

    override fun computeValue(): Long {
        return subPackets.fold(initial = 1) { acc, packet -> acc * packet.expression.computeValue() }
    }

    companion object {
        fun parse(binary: String, startIndex: Int): ParseResult<Expression> {
            val (subPackets, endIndex) = parseSubPackets(binary, startIndex)
            return ParseResult(Product(subPackets), endIndex)
        }
    }
}

data class Minimum(override val subPackets: List<Packet>) : Expression(subPackets) {

    override fun computeValue(): Long {
        return subPackets.minOf { it.expression.computeValue() }
    }

    companion object {
        fun parse(binary: String, startIndex: Int): ParseResult<Expression> {
            val (subPackets, endIndex) = parseSubPackets(binary, startIndex)
            return ParseResult(Minimum(subPackets), endIndex)
        }
    }
}

data class Maximum(override val subPackets: List<Packet>) : Expression(subPackets) {

    override fun computeValue(): Long {
        return subPackets.maxOf { it.expression.computeValue() }
    }

    companion object {
        fun parse(binary: String, startIndex: Int): ParseResult<Expression> {
            val (subPackets, endIndex) = parseSubPackets(binary, startIndex)
            return ParseResult(Maximum(subPackets), endIndex)
        }
    }
}

data class GreaterThan(override val subPackets: List<Packet>) : Expression(subPackets) {

    override fun computeValue(): Long {
        return if (subPackets[0].expression.computeValue() > subPackets[1].expression.computeValue()) {
            1
        } else {
            0
        }
    }

    companion object {
        fun parse(binary: String, startIndex: Int): ParseResult<Expression> {
            val (subPackets, endIndex) = parseSubPackets(binary, startIndex)
            return ParseResult(GreaterThan(subPackets), endIndex)
        }
    }
}


data class LessThan(override val subPackets: List<Packet>) : Expression(subPackets) {

    override fun computeValue(): Long {
        return if (subPackets[0].expression.computeValue() < subPackets[1].expression.computeValue()) {
            1
        } else {
            0
        }
    }

    companion object {
        fun parse(binary: String, startIndex: Int): ParseResult<Expression> {
            val (subPackets, endIndex) = parseSubPackets(binary, startIndex)
            return ParseResult(LessThan(subPackets), endIndex)
        }
    }
}

data class EqualTo(override val subPackets: List<Packet>) : Expression(subPackets) {

    override fun computeValue(): Long {
        return if (subPackets[0].expression.computeValue() == subPackets[1].expression.computeValue()) {
            1
        } else {
            0
        }
    }

    companion object {
        fun parse(binary: String, startIndex: Int): ParseResult<Expression> {
            val (subPackets, endIndex) = parseSubPackets(binary, startIndex)
            return ParseResult(EqualTo(subPackets), endIndex)
        }
    }
}

enum class LengthTypeId {
    TOTAL_LENGTH,
    NUMBER_OF_SUB_PACKETS;

    companion object {
        fun parse(binary: String, startIndex: Int): ParseResult<LengthTypeId> {
            val lengthTypeId = if (binary[startIndex] == '0') {
                TOTAL_LENGTH
            } else {
                NUMBER_OF_SUB_PACKETS
            }
            return ParseResult(lengthTypeId, startIndex + 1)
        }
    }
}


private val hexToBinaryConversionTable: Map<Char, String> = mapOf(
    '0' to "0000",
    '1' to "0001",
    '2' to "0010",
    '3' to "0011",
    '4' to "0100",
    '5' to "0101",
    '6' to "0110",
    '7' to "0111",
    '8' to "1000",
    '9' to "1001",
    'A' to "1010",
    'B' to "1011",
    'C' to "1100",
    'D' to "1101",
    'E' to "1110",
    'F' to "1111",
)

fun hexToBinary(hexadecimal: String): String =
    hexadecimal.map { hexToBinaryConversionTable[it]!! }.joinToString(separator = "")
