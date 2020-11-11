## System

This module emit the actual network status.

### Usage
The `NetworkState` is available through a `NetworkStateRepo`.
`NetworkState` is composed by 2 states: `Available` and `Unavailable`. Each state contains a `Network` object which depending on the type of the state can be of 2 types for `Available`: `Wifi`, `Cellular` and just `Absent` for `Unavailable`.

In order to receive a flow of `NetworkState`, create a `NetworkStateRepo` object and start call the `.flow()` method with it.

Sample code:
```kotlin
private val networkStateRepo = NetworkStateRepo(context)
networkStateRepo.flow().network().collect {
    // Handle the incoming Network object
    foo(it)
}
```
It is possible to get a flow of `Network` instead of receiving `NetworkState` using `.network()` method.