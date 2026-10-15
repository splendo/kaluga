# Bytes

Working with binary data: `ByteArray` utilities and byte orders, odd-width integers, IEEE 11073 medical floats, byte stuffing, string codecs, SHA-1 and CRC.

This is a feature group of [Kaluga](https://github.com/splendo/kaluga), containing the following modules:

| Module | Usage | Artifact | Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|---|---|---|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| [core](core/) | ByteArray utilities, hex conversion, `ByteOrder` and numeric byte encoding | `com.splendo.kaluga.bytes:core` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [integer](integer/) | 24-, 40- and 48-bit integers (`Int24`, `UInt24`, …) | `com.splendo.kaluga.bytes:integer` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [medfloat](medfloat/) | IEEE 11073 medical floats (`MedFloat16`, `MedFloat32`) | `com.splendo.kaluga.bytes:medfloat` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [stuffing](stuffing/) | Byte stuffing schemes (CSafe, Xor, SLIP, COBS) | `com.splendo.kaluga.bytes:stuffing` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [strings](strings/) | Encoding and decoding Chars and Strings to and from bytes | `com.splendo.kaluga.bytes:strings` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [hash](hash/) | SHA-1 digests | `com.splendo.kaluga.bytes:hash` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [crc](crc/) | Cyclic Redundancy Check (CRC) calculation | `com.splendo.kaluga.bytes:crc` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
