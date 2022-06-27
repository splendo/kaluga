## Location

This library allows you to receive user's location.

## Installing
This library is available on Maven Central. You can import Kaluga Location as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:location:$kalugaVersion")
}
```

### Usage
The `LocationState` is available through a `LocationStateRepo` that will automatically detect whether the user has given permission to access Location and has Location enabled.
Each `LocationState` contains either a `KnownLocation` or an `UnknownLocation`.

To use the Location, simply create a `LocationStateRepo` through the `LocationStateRepoBuilder` and start a flow. The `LocationState` flow can easily be converted to a `Location` flow by calling `location()`.

The `Permission.Location` provided to the `LocationStateRepo` will determine the type of `Location` requested. If `background=true` the flow assumes to run in a background thread. Passing `precise=true` triggers a more precise location detection.

By default the user will be automatically prompted to consent to the required `Permissions` and to enable location services. This can be disabled by setting `autoRequestPermission=false` and `autoEnableLocations=false` respectively.

Sample code:
```kotlin
val locationStateRepo = LocationStateRepoBuilder().create(Permissions.Location(background=false, precise=true))
locationStateRepo.flow().location().collect { location ->
    // Handle location change
}
```

### Android Specific Usage
Android handles fetching the location differently depending on how `Permission.Location.background` is set. If `false`, the location service is optimised for use in the foreground, but may not work when called from a `Service` class. If set to `true` however, the request is optimised for usage in a `Service`. Like with any Location service on Android, the `Service` should call `startForeground()` to function properly.

## Testing
Use the [`test-utils-location` module](../test-utils-location) to get a mockable `LocationManager`, `LocationMonitor`, or `LocationStateRepo`.
