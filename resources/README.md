## Resources
This Resource Library for Kaluga contains methods for loading various resources from the platform.
Resources are grabbed using a `String` key associated with a resource type.
Making these resources available on the platform is the responsibility of the platform.

## Installing
This library is available on Maven Central. You can import Kaluga Resources as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:resources:$kalugaVersion")
}
```

## Usage

### String
You can treat any `String` as a key to a localized `String` value.
By default, this loads the String in the `Localizable.strings` (iOS) or `strings.xml` (Android) files declared in the main project scope.
A `StringLoader` class is provided to overwrite this behaviour.

### Color
The `Color` class is associated with `UIColor` (iOS) and `@ColorRes Int` (Android).
You can treat any `String` as a key to a `Color` value.
By default, this loads the Color in the `Color Assets` (iOS) or `color.xml` (Android) files declared in the main project scope.
A `ColorLoader` class is provided to overwrite this behaviour.

Default web colors are available through `DefaultColors`

Colors can also be modified easily. Blend colors by using blend methods such as `backdropColor burn sourceColor ` or change their luminance using `darkenBy` or `lightenBy`. Use `hsl` to get a `HSLColor` instance of the color.

### Image
The `Image` class is associated with `UIImage` (iOS) and `Drawable` (Android).
You can treat any `String` as a key to a `Image` value.
By default, this loads the Image with the corresponding name declared in the main project scope.
An `ImageLoader` class is provided to overwrite this behaviour.

### Font
The `Font` class is associated with `UIFont` (iOS) and `Typeface` (Android).
You can treat any `String` as a key to a `Font` value.
By default, this loads the Font with the same name on iOS and the font resource declared in `font.xml` on Android, as declared in the main project scope.
A `FontLoader` class is provided to overwrite this behaviour.

Unlike the other resources, Font loading may be suspended.

### KalugaLabel and TextStyle
Create a text style with custom font, size and color to share style texts across applications:

```kotlin
val defaultTextStyle = TextStyle(defaultFont, DefaultColors.dimGray, 12.0f)
```

TextStyles can be used to create a `KalugaLabel` that can easily be converted to platform specific labels. In addition to a `KalugaLabel.Plain` you cna also create styled labels using `KalugaLabel.Styled`.
The latter requires a `StyledStringBuilder.Provider`.

```kotlin
KalugaLabel.Plain("Default Text", defaultTextStyle)
val styledStringBuilderProvider: StyledStringBuilder.Provider // provide from platform
KalugaLabel.Styled("Partial Styled text".styled(styledStringBuilderProvider, defaultTextStyle, { Pair(StringStyleAttribute.CharacterStyleAttribute.TextStyle(TextStyle(defaultFont, DefaultColors.red, 12.0f)), IntRange(0, 13)) })),
```

#### Android
*For compose use the [Resources Compose library](../resources-compose)*

You can easily bind a `KalugaLabel` to a `TextView` by using `TextView.bindLabel(label)`
To only apply a `TextStyle` use `TextView.applyTextStyle(textStyle)`

#### iOS
*For SwiftUI see [Kaluga SwiftUI](https://github.com/splendo/kaluga-swiftui)*

You can easily bind a `KalugaLabel` to a `UILabel` or `UITextView` by using `UILabel.bindLabel(label)` or `UITextView.bindLabel(label)` respectively
To only apply a `TextStyle` use `UILabel.applyTextStyle(textStyle)`, `UITextView.bindLabel(label)` or `UITextField.bindLabel(label)` 

### BackgroundStyle
Create a `BackgroundStyle` to apply a background to any view.
A `BackgroundStyle` takes a `FillStyle`, `StrokeStyle` and `Shape` to describe the background.

`FillStyle` can be either `Solid` or `Gradient`, where `Gradient` requires a `GradientStyle` to be created.
`StrokeStyle` can be either `None`, removing all strokes, or `Stroke`, taking a width and `Color` to describe the stroke
`Shape` determines the shape of the Background. It can be either `Rectangle` or `Oval`. The `Rectangle` shape allows for rounded corners to be set.

The `GradientStyle` can be `Linear`, `Radial`, or `Angular` and is created from either a list of `Color` or `ColorPoint`, where the `ColorPoint` specifies the position of the color on a scale of `0.0`- `1.0`

```kotlin
BackgroundStyle(
    BackgroundStyle.FillStyle.Solid(DefaultColors.deepSkyBlue),
    BackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.white),
    BackgroundStyle.Shape.Rectangle(10.0f, setOf(BackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT, BackgroundStyle.Shape.Rectangle.Corner.BOTTOM_LEFT))
)
```

#### Android
*For compose use the [Resources Compose library](../resources-compose)*

Apply a `BackgroundStyle` to any `View` using `View.applyBackgroundStyle(backgroundStyle`

#### iOS
*For SwiftUI see [Kaluga SwiftUI](https://github.com/splendo/kaluga-swiftui)*

Apply a `BackgroundStyle` to any `UIView` using `UIView.applyBackgroundStyle(backgroundStyle)` or to a `CALayer` using `CALayer.applyBackgroundStyle(backgroundStyle, bounds)`

### KalugaButton and ButtonStyle
Like `TextStyle` for a Label, a `ButtonStyle` can be created for a button.

A `ButtonStyle` takes a font, textSize, text alignment and a `ButtonStateStyle` for its default, pressed, and disabled states.
The `ButtonStateStyle` consists of a text color and a `BackgroundStyle` describing the background of the button.

```kotlin
ButtonStyle(
    defaultFont,
    12.0f,
    defaultStyle = ButtonStateStyle(DefaultColors.white, DefaultColors.deepSkyBlue, BackgroundStyle.Shape.Rectangle(10.0f, setOf(BackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT, BackgroundStyle.Shape.Rectangle.Corner.BOTTOM_LEFT))),
    pressedStyle = ButtonStateStyle(DefaultColors.azure, DefaultColors.lightSkyBlue, BackgroundStyle.Shape.Rectangle(5.0f, setOf(BackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT, BackgroundStyle.Shape.Rectangle.Corner.BOTTOM_RIGHT))),
    disabledStyle = ButtonStateStyle(DefaultColors.black, DefaultColors.lightGray, BackgroundStyle.Shape.Rectangle(10.0f))
)
```

Convenience methods exist to create plain colored buttons or even just textButtons

```
val redBackgroundButtonStyle = ButtonStyle(
    defaultTextStyle,
    backgroundColor = DefaultColors.red,
    pressedBackgroundColor = DefaultColors.maroon,
    disabledBackgroundColor = DefaultColors.lightGray
)

val defaultButtonStyle = ButtonStyle(defaultTextStyle)
```

Like `KalugaLabel` a `KalugaButton` exists to support converting to platform specific UI. Similar to the label a `KalugaButton` can be either `Plain` or `Styled` and takes a closure to determine its action

```kotlin

KalugaButton.Plain("Press me", redBackgroundButtonStyle,  isEnabled = true) {
    // Handle button press
}
val styledStringBuilderProvider: StyledStringBuilder.Provider // provide from platform
KalugaButton.Styled(
    "Partial Styled text".styled(styledStringBuilderProvider, defaultTextStyle, { Pair(StringStyleAttribute.CharacterStyleAttribute.TextStyle(TextStyle(defaultFont, DefaultColors.red, 12.0f)), IntRange(0, 13)) })
    defaultButtonStyle,
    isEnabled = true
) {
    // Handle button press
}
```

#### Android
*For compose use the [Resources Compose library](../resources-compose)*

Bind a `KalugaButton` to a `Button` by using `Button.bindButton(kalugaButton)`
It is also possible to apply a `ButtonStyle` using `Button.applyButtonStyle(style)`. This method also allows you to pass a `RippleStyle` to modify the ripple behaviour.

#### iOS
*For SwiftUI see [Kaluga SwiftUI](https://github.com/splendo/kaluga-swiftui)*

Bind a `KalugaButton` to a `UIButton` by using `UIButton.bindButton(kalugaButton)`
It is also possible to apply a `ButtonStyle` using `UIButton.applyStyle(style)`
