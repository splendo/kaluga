# Resources Compose
Compose extensions for [Resources](../resources)

## Installing
This library is available on Maven Central. You can import Kaluga Resources Compose as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:resources-compose:$kalugaVersion")
}
```

## KalugaColor
Convert a `KalugaColor` to a composable color using `KalugaColor.composable`

## KalugaImage
Convert a `KalugaImage` to a composable image using `KalugaImage.Composable(String?, Modifier, Alignment, ContentScale, Float, ColorFilter?)`
Convert a `TintedImage` to a composable image using `TintedImage.Composable(String?, Modifier, Alignment, ContentScale, Float)`

## KalugaLabel
To use a `KalugaLabel` in compose, simply call `KalugaLabel.Composable(Modifier)` in your UI code.

## KalugaBackgroundStyle
To use `KalugaBackgroundStyle` in compose, apply the `backgroundStyle` to the `Modifier` managing your view.

## KalugaButton
To use a `KalugaButton` in compose, simply call `KalugaButton.Composable(Modifier, ButtonElevation, PaddingValues)` in your UI code.
