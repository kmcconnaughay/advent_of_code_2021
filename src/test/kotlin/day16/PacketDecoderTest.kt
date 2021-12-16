package day16

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PacketDecoderTest {

    @Test
    fun literalPacket_parsesPacket() {
        val binary = hexToBinary("D2FE28")

        val packet = Packet.parse(binary)

        assertEquals(Packet(Header(Version(6), TypeId.LITERAL), Literal(2021)), packet.value)
        assertEquals(21, packet.endIndex)
    }

    @Test
    fun subPackets_totalLengthLengthTypeId_parsesAllPackets() {
        val binary = hexToBinary("38006F45291200")

        val packet = Packet.parse(binary)

        assertEquals(
            Packet(
                Header(Version(1), TypeId.LESS_THAN),
                LessThan(
                    listOf(
                        Packet(Header(Version(6), TypeId.LITERAL), Literal(10)),
                        Packet(Header(Version(2), TypeId.LITERAL), Literal(20))
                    )
                )
            ),
            packet.value
        )
    }

    @Test
    fun subPackets_numSubPacketsLengthTypeId_parsesAllPackets() {
        val binary = hexToBinary("EE00D40C823060")

        val packet = Packet.parse(binary)

        assertEquals(
            Packet(
                Header(Version(7), TypeId.MAXIMUM),
                Maximum(
                    listOf(
                        Packet(Header(Version(2), TypeId.LITERAL), Literal(1)),
                        Packet(Header(Version(4), TypeId.LITERAL), Literal(2)),
                        Packet(Header(Version(1), TypeId.LITERAL), Literal(3))
                    )
                )
            ),
            packet.value
        )
    }

    @Test
    fun sumOfVersionNumbers_traversePacketTreeToSum() {
        assertEquals(16, sumOfVersionNumbers(Packet.parse(hexToBinary("8A004A801A8002F478")).value))
        assertEquals(12, sumOfVersionNumbers(Packet.parse(hexToBinary("620080001611562C8802118E34")).value))
        assertEquals(23, sumOfVersionNumbers(Packet.parse(hexToBinary("C0015000016115A2E0802F182340")).value))
        assertEquals(31, sumOfVersionNumbers(Packet.parse(hexToBinary("A0016C880162017C3686B18A3D4780")).value))
    }

    @Test
    fun computeValue_evaluatesExpression() {
        assertEquals(3, Packet.parse(hexToBinary("C200B40A82")).value.expression.computeValue())
        assertEquals(54, Packet.parse(hexToBinary("04005AC33890")).value.expression.computeValue())
        assertEquals(7, Packet.parse(hexToBinary("880086C3E88112")).value.expression.computeValue())
        assertEquals(9, Packet.parse(hexToBinary("CE00C43D881120")).value.expression.computeValue())
        assertEquals(1, Packet.parse(hexToBinary("D8005AC2A8F0")).value.expression.computeValue())
        assertEquals(0, Packet.parse(hexToBinary("F600BC2D8F")).value.expression.computeValue())
        assertEquals(0, Packet.parse(hexToBinary("9C005AC2F8F0")).value.expression.computeValue())
        assertEquals(1, Packet.parse(hexToBinary("9C0141080250320F1802104A08")).value.expression.computeValue())
    }
}