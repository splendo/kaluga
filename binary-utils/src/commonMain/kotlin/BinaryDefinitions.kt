/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

/**
 * The Octet is a unit of information that consists of eight Bits.
 * The term is often used when the term Byte might be ambiguous or confusing.
 * Especially in abbreviations where both "Bit" and "Byte" can be replaced by same letter "B"
 *
 * The Byte has historically been used for storage units of a variety of sizes.
 * Curiously enough, up until 1964 or so, a byte was, in fact, six bits long.
 * In 1963, the telecom industry, increasingly constrained by the limitation of 6-bit encoding,
 * came up with ASCII standard. The new standard used 7 bits per symbol
 * and an optional eighth for parity. That resulted in a split between the computing and
 * telecommunication industries as to the length of the most basic unit of information.
 *
 * The term Octet is frequently used in the Request for Comments (RFC) publications of
 * the Internet Engineering Task Force to describe storage sizes of network protocol parameters.
 * The earliest example is RFC 635 from 1974. IETF needed a precise and universal language
 * as RFCs often describe protocols for communicating between various machines with different
 * architectures.
 *
 * That practice has been adopted by other bodies in wide range of communication and other protocols.
 * One of the many examples is Bluetooth Core Specification by The Bluetooth Special Interest Group.
 */
typealias Octet = Byte

/**
 * Endianness
 *
 * Endianness describe how computers organize the most basic units (Bits and Octets) that make up numbers.
 * It is the order or sequence of Bits or Octets that represents a number we want to store or transmit.
 *
 * Endianness can be seen as statement of this format:
 * X (Most or Least) Significant Y (Bit or Byte) first
 *
 * The most important thing to remember about Endianness is that statement "X Significant Y first"
 * will render different Bit or Octet representations when you:
 * * Draw(write) data; X Significant Y wrote first (on the left, for the left-to-right languages)
 * * Store data X Significant Y at first address (lower memory address)
 * * Transmit data; X Significant Y transmitted first (arriving first)
 *
 * ## Most- vs least-significant Bit first
 * The expressions *most significant bit first* (Big Endian Bit order)
 * and *least significant bit first" (Little Endian Bit order) are indications on the ordering of
 * the sequence of the bits in the bytes sent over a transmission medium like:
 * * a wire in a serial transmission protocol
 * * a stream in e.g. an audio stream
 * * a radio frequency in e.g a Bluetooth protocol
 *
 * "Most significant bit first" means that the most significant bit will arrive first:
 * hence e.g. the hexadecimal number `0x12`, `00010010` in binary representation,
 * will arrive as the sequence `0 0 0 1 0 0 1 0`.
 *
 * "Least significant bit first" means that the least significant bit will arrive first:
 * hence e.g. the same hexadecimal number `0x12`, again `00010010` in binary representation,
 * will arrive as the (reversed) sequence `0 1 0 0 1 0 0 0`.
 *
 * Whenever binary data is transmitted, usually, it is the role of a (Hardware) Controller and/or
 * (Software) Driver to translate bit ordering between one used in a transmission medium and
 * one used in the host System
 *
 * ## Bit field order
 * A bit field is a data structure that consists of one or more adjacent bits which
 * have been allocated to hold a sequence of bits, stored so that any single bit or
 * group of bits within the group can be set or inspected.
 *
 * When dealing with bit fields Endianness dictates the order of sub-group within a bit field.
 * In other words it dictates the direction of parsing a bitfield.
 * For Big Endian order first sub-group starts at the MSB where for Little Endian it starts at LSB.
 *
 * Important: Bit field Endianness **does not** change the order of bits within any sub-group.
 * For example:
 ```
For 8 Bit wide bit field holding two 4 Bit sub-groups
+----------------------------+-----+---+---+---+---+---+----+-----+
| Binary representation (0b) | 1   | 0 | 0 | 1 | 0 | 1 |  0 | 1   |
+----------------------------+-----+---+---+---+---+---+----+-----+
| Significance               | MSB |   |   |   |   |   |    | LSB |
+----------------------------+-----+---+---+---+---+---+----+-----+
| Index                      | 7   | 6 | 5 | 4 | 3 | 2 |  1 | 0   |
+----------------------------+-----+---+---+---+---+---+----+-----+
In Big Endian order
+----------------------------+-----+---+---+---+---+---+----+-----+
| sub-group A                | 3   | 2 | 1 | 0 | - | - |  - | -   |
+----------------------------+-----+---+---+---+---+---+----+-----+
| sub-group B                | -   | - | - | - | 3 | 2 |  1 | 0   |
+----------------------------+-----+---+---+---+---+---+----+-----+
In Little Endian order
+----------------------------+-----+---+---+---+---+---+----+-----+
| sub-group A                | -   | - | - | - | 3 | 2 |  1 | 0   |
+----------------------------+-----+---+---+---+---+---+----+-----+
| sub-group B                | 3   | 2 | 1 | 0 | - | - |  - | -   |
+----------------------------+-----+---+---+---+---+---+----+-----+
 ```
 *
 * @see Octet
 * @see [BinarySignificance]
 */
enum class Endianness {
    MOST_SIGNIFICANT_FIRST,
    LEAST_SIGNIFICANT_FIRST;

    companion object {

        /**
         * Most Significant Bit to Least Significant Bit
         * Describes the order of Bits within any unit of information.
         *
         * @see [BinarySignificance]
         */
        val MSB_TO_LSB = MOST_SIGNIFICANT_FIRST

        /**
         * Least Significant Bit to Most Significant Bit
         * Describes the order of Bits within a sequence of Bits.
         *
         * @see [BinarySignificance]
         */
        val LSB_TO_MSB = LEAST_SIGNIFICANT_FIRST

        /**
         * Most Significant Octet to Least Significant Octet
         * Describes the order of Octet within a sequence of Octets.
         *
         * @see Octet
         * @see [BinarySignificance]
         */
        val MSO_TO_LSO = MOST_SIGNIFICANT_FIRST

        /**
         * Least Significant Octet to Most Significant Octet
         * Describes the order of Octet within a sequence of Octets.
         *
         * @see Octet
         * @see [BinarySignificance]
         */
        val LSO_TO_MSO = LEAST_SIGNIFICANT_FIRST
    }
}
/**
 * Bit significance
 *
 * In computing, the least significant bit (LSB) is the bit position in
 * a binary integer representing the binary 1st place of the integer.
 * LSB is giving the units value, that is, determining whether the number is even or odd.
 *
 * Similarly, the most significant bit (MSB) represents the highest-order place of the binary integer.
 * That means this bit position in a binary number is having the greatest value.
 *
 * The LSB is sometimes (for the most common bit numbering scheme) referred to as
 * the low-order bit or right-most bit, due to the convention in positional notation of
 * writing less significant digits further to the right.
 * The MSB is similarly referred to as the high-order bit or left-most bit.
 *
 * In both cases, the LSB and MSB correlate directly to the
 * least significant digit and most significant digit of a decimal integer.
 *
 * Example
```
Encoding decimal value 149
+----------------------------+-----+---+---+---+---+---+----+-----+
| Binary representation (0b) | 1   | 0 | 0 | 1 | 0 | 1 |  0 | 1   |
+----------------------------+-----+---+---+---+---+---+----+-----+
| Significance               | MSB |   |   |   |   |   |    | LSB |
+----------------------------+-----+---+---+---+---+---+----+-----+
```
 * For more information see:
 *
 * @see [BitNumberingScheme]
 */
enum class BinarySignificance {
    /**
     * The Most Significant Bit (MSB) or Octet (MSO)
     *
     * @see Octet
     * @see [BinarySignificance]
     */
    MOST_SIGNIFICANT,
    /**
     * The Least Significant Bit (LSB) or Octet (LSO)
     *
     * @see Octet
     * @see [BinarySignificance]
     */
    LEAST_SIGNIFICANT;

    companion object {
        /** The Most Significant Bit (MSB)
         *
         * @see MOST_SIGNIFICANT
         * @see [BinarySignificance]
         */
        val MSB = MOST_SIGNIFICANT

        /** The Most Significant Bit (MSB)
         *
         * @see MOST_SIGNIFICANT
         * @see [BinarySignificance]
         */
        val MOST_SIGNIFICANT_BIT = MOST_SIGNIFICANT

        /** The Most Significant Bit (MSB)
         *
         * @see MOST_SIGNIFICANT
         * @see [BinarySignificance]
         */
        val HIGH_ORDER_BIT = MOST_SIGNIFICANT

        /** The Least Significant Bit (LSB)
         *
         * @see LEAST_SIGNIFICANT
         * @see [BinarySignificance]
         */
        val LSB = LEAST_SIGNIFICANT

        /** The Least Significant Bit (LSB)
         *
         * @see LEAST_SIGNIFICANT
         * @see [BinarySignificance]
         */
        val LEAST_SIGNIFICANT_BIT = LEAST_SIGNIFICANT

        /** The Least Significant Bit (LSB)
         *
         * @see LEAST_SIGNIFICANT
         * @see [BinarySignificance]
         */
        val LOW_ORDER_BIT = LEAST_SIGNIFICANT

        /** The Most Significant Octet (MSO)
         *
         * @see Octet
         * @see MOST_SIGNIFICANT
         * @see [BinarySignificance]
         */
        val MSO = MOST_SIGNIFICANT

        /** The Most Significant Octet (MSO)
         *
         * @see Octet
         * @see MOST_SIGNIFICANT
         * @see [BinarySignificance]
         */
        val MOST_SIGNIFICANT_OCTET = MOST_SIGNIFICANT

        /** The Least Significant Octet (LSO)
         *
         * @see Octet
         * @see MOST_SIGNIFICANT
         * @see [BinarySignificance]
         */
        val LSO = LEAST_SIGNIFICANT

        /** The Least Significant Octet (LSO)
         *
         * @see Octet
         * @see MOST_SIGNIFICANT
         * @see [BinarySignificance]
         */
        val LEAST_SIGNIFICANT_OCTET = LEAST_SIGNIFICANT
    }
}

/**
 * Bit numbering scheme
 *
 * In computing, bit numbering is the convention used to identify the bit positions in
 * a binary number or a container of such a value.
 * The bit number starts with zero and is incremented by one for each subsequent bit position.
 *
 * In most common cases numbering scheme follows the Bit indexing notation.
 *
 * ## Bit Indexing
 * Bit indexing correlates to the positional notation of the value in base 2 numeral system (binary numeral system).
 * For this reason, bit index is not affected by how the value is stored on the device,
 * such as the value's byte order. Rather, it is a property of the numeric value in binary itself.
 *
 * For the Left-to-Right languages the convention in positional notation is to write
 * less significant digits further to the right.
 * It is analogous to the least significant digit of a decimal integer,
 * which is the digit in the ones (right-most) position.
 *
 * ## Most common numbering scheme
 * In the most cases the bit numbering starts at zero for the least significant bit (LSB)
 * and correlates with Bit index
 *
 * correlates with the positional notation of the values in base 2 numeral system (binary numeral system).
 * This bit numbering method has the advantage that for any unsigned number
 * the value of the number can be calculated by using exponentiation with the bit number and a base of 2.
 * That means the bit number is simply the exponent for the corresponding bit weight in base-2 (such as in 2^31..2^0).
 *
 * Only a few CPU manufacturers have assigned bit numbers the opposite way (which is not the same as different endianness).
 * In any case, the least significant bit itself remains unambiguous as the unit bit.
 *
 * Following table illustrates differences:
```
Encoding decimal value 149

+-----------------------------------+-----+-----+-----+-----+-----+-----+-----+-----+
| Binary representation (0b)        | 1   | 0   | 0   | 1   | 0   | 1   | 0   | 1   |
+-----------------------------------+-----+-----+-----+-----+-----+-----+-----+-----+
| Bit weight for position n ( 2^n ) | 2^7 | 2^6 | 2^5 | 2^4 | 2^3 | 2^2 | 2^1 | 2^0 |
+-----------------------------------+-----+-----+-----+-----+-----+-----+-----+-----+
| Significance                      | MSB |     |     |     |     |     |     | LSB |
+-----------------------------------+-----+-----+-----+-----+-----+-----+-----+-----+
| Index                             | 7   | 6   | 5   | 4   | 3   | 2   | 1   | 0   |
+-----------------------------------+-----+-----+-----+-----+-----+-----+-----+-----+
| LSB 0 numbering                   | 7   | 6   | 5   | 4   | 3   | 2   | 1   | 0   |
+-----------------------------------+-----+-----+-----+-----+-----+-----+-----+-----+
| MSB 0 numbering                   | 0   | 1   | 2   | 3   | 4   | 5   | 6   | 7   |
+-----------------------------------+-----+-----+-----+-----+-----+-----+-----+-----+
```
 */
enum class BitNumberingScheme {
    /**
     * When the bit numbering starts at zero for the least significant bit (LSB)
     */
    LSB_0,

    /**
     * When the bit numbering starts at zero for the most significant bit (MSB)
     */
    MSB_0;

    companion object {
        /**
         * Index 0 at LSB
         *
         * @see LSB_0
         */
        val ZERO_AT_LSB = LSB_0

        /**
         * Index 0 at MSB
         *
         * @see MSB_0
         */
        val ZERO_AT_MSB = MSB_0
    }
}
