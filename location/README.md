## Location

This library allows you to receive user's location.

### Usage

`LocationFlowable` object is used to provide location. And `LocationFlowable.Builder` to build.

On iOS you have to provide `CLLocationManager` into builder's initializer.

```kotlin
val manager = CLLocationManager()
val builder = LocationFlowable.Builder(manager)
```
On Android you have to provide `FusedLocationProviderClient` into builder's initializer.

```kotlin
val client = LocationServices.getFusedLocationProviderClient(this)
val builder = LocationFlowable.Builder(client)
```

After short setup of builder you can create location flowable object you can start collecting Location objects:

```kotlin
val location = builder.create()
location.flow().collect {
    println("location: $it")
}
```
