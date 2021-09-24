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

### BackgroundStyle

### KalugaButton and ButtonStyle

