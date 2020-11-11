## System

This module emit the actual network status.

### Usage
The `NetworkState` is available through a `NetworkStateRepo`.
`NetworkState` is composed by 2 states: `Available` and `Unavailable`. Each state contains `Network` objects. In case of `Available` there are two types : `Wifi` and `Cellular`, while in case of `Unavailable` the type is just `Absent`.

In order to receive a flow of `NetworkState`, create a `NetworkStateRepo` object and start call the `.flow()` method with it.

Sample code:
```kotlin
private val networkStateRepo = NetworkStateRepo(context)
networkStateRepo.flow().network().collect {
    // Handle the incoming Network object
    foo(it)
}
```
It is possible to get a flow of `Network` instead of receiving `NetworkState` by using `.network()` method.