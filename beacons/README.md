## Beacons

A library allows you to detect BLE beacons.
It currently supports iBeacon and Eddystone beacons for both iOS and Android.

Android implementation is based on [Altbeacon library](https://altbeacon.github.io/android-beacon-library/).

For iOS the only possible way to work with iBeacons is [CoreLocation](https://developer.apple.com/documentation/corelocation/determining_the_proximity_to_an_ibeacon_device). For the Eddystone support we use the [Swift code provided by Google](https://github.com/google/eddystone/tree/master/tools/ios-eddystone-scanner-sample). It is added as a native dependency to iOS implementation.

### Usage

You will need location permission on both iOS and Android in order to use this module.

Start with creating `BeaconMonitor` (provide the platform specific `BeaconScanner` implementation to its constructor).

Use `BeaconMonitor.subscribe(beaconId, listener)` to subscribe to specific beacon ID.

To stop listening for specific beacon you can use `BeaconMonitor.unsubscribe(beaconId)`.

### Further developement

#### Improvements

* Fix iOS sample app to compile properly.
* Try to remove `@Suppress("NO_ACTUAL_FOR_EXPECT")` from `BeaconScanner`. It can cause runtime errors.
* Test all device-beacon types combinations on real devices (device types - iOS, Android; beacon types - iBeacon, Eddystone URL, Eddystone UID, Eddystone TLM, Eddystone EID)
* Refactor `BeaconScanner` to extract common logic
* Add Gradle task to remove `iOSMain/native/Carthage folder` and `Cartfile.resolved` when cleaning project
* Consider using Carthage dependency [locally](https://github.com/Carthage/Carthage/issues/1458) or alternatively enable cache for Carthage 


#### New features

* Background scanning
* Support ranging to get list of all beacons around, sorted by distance


### Useful links

* [iBeacons are not available via CoreBluetooth in iOS](https://stackoverflow.com/a/20391844)
* [C interop in Kotlin MPP](https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#cinterop-support)
* [C interop in Gradle](https://kotlinlang.org/docs/reference/native/gradle_plugin.html#using-cinterop)
* [How to create iOS framework with Carthage](https://medium.com/better-programming/how-to-create-a-framework-with-carthage-support-c30b596d3a7a)
* [Adding iOS framework as MPP dependency](https://gist.github.com/LouisCAD/3a9d8fc1f8dc118b9ae5fb8f98abe118)
