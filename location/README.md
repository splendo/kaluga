## Location

This library allows you to receive user's location.

### Usage
`LocationFlowable` object is used to provide location.

On iOS you have to provide `CLLocationManager` into `addCLLocationManager` function call.

On Android you have to provide `FusedLocationProviderClient` into `setFusedLocationProviderClient` function call.

After short setup of location flowable object you can start collecting Location objects:

```kotlin
val location = LocationFlowable()
// Set platform
location.flow().collect {
    println("location: $it")
}
```
