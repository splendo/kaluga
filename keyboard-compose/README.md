## Keyboard-compose
This Android library contains composable functions to work with Kaluga keyboard.

## Installing
This library is available on Maven Central. You can import Kaluga Keyboard Compose as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.keyboard-compose:$kalugaVersion")
}
```

## Usage

Create a `ComposeKeyboardManager.Builder` and add it to the `LifecycleSubscribables` of a `BaseLifecycleViewModel`.
Bind the ViewModel to your composable view using `ViewModelComposable`.

To show the keyboard, add a `FocusRequester` to a view using
```kotlin
modifier = Modifier
    .focusRequester(focusRequester),
```

And focus on it using `ComposeFocusHandler(focusRequester)`
