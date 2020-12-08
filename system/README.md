
# System

This module aim to cover system APIs such as network, audio, battery etc...

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
	networkStateRepo.flow().network().collect {
	    // Handle the incoming Network object
	    foo(it)
	}
}
```
It would be enough to call `networkStateRepo.flow().collect {...}` and receive updates of `NetworkState`, but it is possible to receive `Network` object using `.network()` function (like in the sample code above).