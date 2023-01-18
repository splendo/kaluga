# Resources-compose
[Android Data Binding](https://developer.android.com/topic/libraries/data-binding) extensions for [Resources](../resources)

This module requires the Android project using it declaring the [Kapt plugin](https://kotlinlang.org/docs/kapt.html).

## KalugaLabel ad TextStyle
Use `app:kalugaLabel` to bind a `KalugaLabel` to a `TextView`.

`TextStyle` can be applied using `app:textStyle` as well. To support autosize text, use

```
app:autoTextStyle=textStyle
app:autoTextMinScalingFactor=scalingFactor
```

## BackgroundStyle
Use `app:backgroundStyle` to bind a `BackgroundStyle` to a `View`

## KalugaButton
Use `app:kalugaButton` to bind a `KalugaButton` to a `Button`

## Image
Use `app:image` to bind an `Image` to an `ImageView` 
