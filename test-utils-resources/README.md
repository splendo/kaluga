# Test Utils Resources

This library adds support for testing the [`resources` module](../resources) to [`test-utils`](../test-utils-base)

## Installing
This library is available on Maven Central. You can import Kaluga Test Utils Resources as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:test-utils-resources:$kalugaVersion")
}
```

## Mocks
This library contains mock classes for `StringLoader`, `FontLoader`, `KalugaColorLoader` and `ImageLoader`.

Call `mockColor`, `mockFont`, or `mockImage` to generate mocks for `KalugaColor`, `Font`, or `Image` respectively.
