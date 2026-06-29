# Kaluga Example — macOS

A minimal AppKit host that loads the Compose Multiplatform root from `:shared`. The list shows
only the features whose underlying Kaluga module supports macOS (`Feature.availableOnMacOS == true`).

## Build & run

The Xcode project runs `./gradlew :example:shared:embedAndSignAppleFrameworkForXcode` as a
pre-build script (mirrors the iOS project). That task reads `$CONFIGURATION` / `$SDK_NAME` /
`$ARCHS` from Xcode, links the correct `KalugaExampleShared.framework` variant under
`shared/build/xcode-frameworks/<configuration>/<sdk>/`, and embeds + signs it into the app bundle.
You do not need to run a separate Gradle command first.

Compose Multiplatform only publishes for `macosArm64` — Intel Macs are not supported here.

1. Generate the Xcode project (one-off — re-run after `project.yml` edits):

   ```sh
   brew install xcodegen   # if missing
   xcodegen generate
   ```

2. Open `Demo.xcodeproj`, pick the **Demo** scheme on a macOS run destination, and hit ⌘R.

## How the host wires Compose

Compose Multiplatform owns the `NSApplication` lifecycle on macOS and creates its own `NSWindow` via
the `Window { … }` composable. The Swift entry point therefore is a plain AppKit `NSApplication.run()`
rather than `@main App`; from `applicationDidFinishLaunching` it calls Kotlin's
`MainViewControllerKt.startMainWindow { … }` (defined in
`shared/src/macosMain/.../ui/MainViewController.kt`) to mount the CMP root.

## Feature coverage

The CMP feature list (`AppRootScreen`) filters to entries where `Feature.availableOnMacOS == true`.
At the time of writing those are:

- **Links** — opens a URL via `NSWorkspace`.
- **System → Network** — exposes `kaluga.system.network.state`.
- **DateTime / Timer** — `kaluga.datetime.timer` + the macOS variant of `KalugaDateFormatter`.
- **Permissions list + detail** — pulled through `kaluga.permissions`.
- **Location** — `kaluga.location` with the default macOS `CLLocationManager` builder.
- **Bluetooth (client scan)** — `kaluga.bluetooth` with `CoreBluetooth`. The server pane is a
  placeholder until the kaluga-server APIs are exercised here.
- **Scientific** — placeholder until the converter UI is ported from `ScientificViewModel`.

UI-only Kaluga features (Alerts, Architecture demo, Beacons, DateTimePicker, HUD, Keyboard, Media,
Resources) live in `:mobileshared` and are intentionally **not** exposed here.
