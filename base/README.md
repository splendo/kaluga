# Base

Core building blocks of Kaluga: threading, state management, flowables, collections, decimals, byte utilities, CRC, localization and formatting.

This is a feature group of [Kaluga](https://github.com/splendo/kaluga), containing the following modules:

| Module | Usage | Artifact | Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|---|---|---|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| [core](core/) | Threading, flowables and concurrent collections | `com.splendo.kaluga.base:core` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [state](state/) | State machines (`KalugaState`, `StateRepo`) | `com.splendo.kaluga.base:state` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [decimal](decimal/) | High-precision decimal arithmetic | `com.splendo.kaluga.base:decimal` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [bytes](bytes/) | ByteArray utilities, hex conversion and numeric byte encoding | `com.splendo.kaluga.base:bytes` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [crc](crc/) | Cyclic Redundancy Check (CRC) calculation | `com.splendo.kaluga.base:crc` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [i18n](i18n/) | Locales, unit systems and locale-aware String casing | `com.splendo.kaluga.base:i18n` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [formatting](formatting/) | Formatting and parsing of numbers and strings | `com.splendo.kaluga.base:formatting` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [test-utils](test-utils/) | Test helpers built on top of base | `com.splendo.kaluga.base:test` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
