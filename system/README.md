
# System

This module aim to cover system APIs such as network, audio, battery etc...

## Installing
This library is available on Maven Central. You can import Kaluga System as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:system:$kalugaVersion")
}
```

## Network
### Usage
The `NetworkState` is available through a `NetworkStateRepo`.
`NetworkState` is composed by 3 states and each state contains `Network` objects:
    - `Available`: it can be of 2 types `Wifi` and `Cellular`. Both of them contains also a flag `isExpensive` that specify if the network is available via hotspot.
    - `Unavailable`: the network will be of type `Absent`.
    - `Unknown`: in this case the network could be either `Unknown.WithLastKnownNetwork` or `Unknown.WithoutLastKnownNetwork`. The difference between them is that `WithLastKnownNetwork` contains both a reason and the last known network before the state was Unknown.

In order to receive a flow of `NetworkState`, create a `NetworkStateRepoBuilder` object and call the `create()` method which returns a `NetworkStateRepo`, then start a flow using the `NetworkStateRepo` and receive updates everytime the state changes.

Sample code:
```kotlin
fun bar(networkStateRepoBuilder: NetworkStateRepoBuilder) {
	val networkStateRepo = networkStateRepoBuilder.create()
	networkStateRepo.flow().collect {
	    // Handle the incoming Network object
	    foo(it)
	}
}
```

In case receiving a `NetworkState` is too much, it is possible to use `network()` function which returns a `Network` object.

```kotlin
fun bar(networkStateRepoBuilder: NetworkStateRepoBuilder) {
	val networkStateRepo = networkStateRepoBuilder.create()
	networkStateRepo.flow().network().collect {
	    doSomethingWithNetwork(it)
	}
}

fun doSomethingWithNetwork(network: Network) {
    ...
}
```

Or `online()` method which returns `true` when the connection is `Available`.

```kotlin
fun bar(networkStateRepoBuilder: NetworkStateRepoBuilder) {
	val networkStateRepo = networkStateRepoBuilder.create()
	networkStateRepo.flow().online().collect {
	    if (it) {
	        // Network is Available
	    } else {
	        // Network is Unavailable or Unknown
	    }
	}
}
```
