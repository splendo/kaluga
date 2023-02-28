# Resources Data Binding
[Android Data Binding](https://developer.android.com/topic/libraries/data-binding) extensions for [Resources](../resources)

This module requires the Android project using it declaring the [Kapt plugin](https://kotlinlang.org/docs/kapt.html).

## Installing
This library is available on Maven Central. You can import Kaluga Resources Data Binding as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:resources-databinding:$kalugaVersion")
}
```

## KalugaLabel ad KalugaTextStyle
Use `app:kalugaLabel` to bind a `KalugaLabel` to a `TextView`.

`KalugaTextStyle` can be applied using `app:textStyle` as well. To support autosize text, use

```
app:autoTextStyle=textStyle
app:autoTextMinScalingFactor=scalingFactor
```

## KalugaBackgroundStyle
Use `app:backgroundStyle` to bind a `KalugaBackgroundStyle` to a `View`

## KalugaButton
Use `app:kalugaButton` to bind a `KalugaButton` to a `Button`

## KalugaImage
Use `app:image` to bind a `KalugaImage` to an `ImageView`
Use `app:tintedImage` to bind a `TintedImage` to an `ImageView` 
