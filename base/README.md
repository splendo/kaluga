## Base
This Base Library for Kaluga contains Data types and accessors for easy use across all Kaluga projects.

## Installing
This library is available on Maven Central. You can import Kaluga Base as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:base:$kalugaVersion")
}
```

### Threading
You can run code on the OS Main Thread by using `runOnMain`. Use the `MainQueueDispatcher` to access the Main queue of the OS.

### Data Accessors
- Use `byOrdinalOrDefault` to get an Enum value by ordinal or the default value if no such value exists.
- (Android Only) Use the `ApplicationHolder` class to get and set the current `Application`. This is useful for default access to the ApplicationContext

### Data Converters
- Convert a `ByteArray` to a hexadecimal String using `toHexString()`
- (iOS Only) Convert `NSData` to `ByteArray` and vice versa using `toByteArray()` and `toNSData()` respectively.

### Flowable and State
The Kaluga library offers usage of Flows and State Machines.

A `Flowable` is an accessor to share data with multiple `Flow`s at the same time.
After creating a `Flowable` call `flow()` to get a new flow and start observing the data sent to the flowable.
New data can be sent to the flowable using `set()`.
The flowable can be closed using `close()`. This will automatically close all the flows derived from this flowable.

```kotlin
val flowable: Flowable<String> = ...
val handleValue = launch {
    flowable.flow().collect { value ->
        // Handle value
    }
}
launch {
    flowable.set("Hello World")
    // handleValue receives "Hello World"
    flowable.close()
    // handleValue finishes
}
```

The `StateRepo` takes a `Flowable` to manage a state machine.
The states the state repo can be in are described by a `State` class.
As with flowable `flow()` will result in a new `Flow` to observe the current state.
However, in order to ensure only acceptable state transitions occur it is not possible to directly set a new state.
Instead `takeAndChangeState()` should be called.
This method takes a closure that determines the next state based on the current state.
The closure should either return a new state created from the given state or `remain` to cancel state transitions.
If additional calls to takeAndChangeState occur while transitioning to a new state they will be enqueued until the pending transition has completed.

It is recommended to only allow `State` classes to create the new state that they can transition into.

```kotlin
sealed class SomeState : State<SomeState> {

    class StateOne internal constructor() : SomeState() {
        val becomeTwo = suspend { StateTwo() }
    }

   class StateTwo internal constructor() : SomeState()

}

val stateRepo: StateRepo<SomeState> = ...
launch {
    stateRepo.takeAndChangeState { state ->
        when (val state) {
            is StateOne -> state.becomeTwo
            is StateTwo -> state.remain
        }
    }
}
```

During transition the following callbacks may be implemented. The callbacks are called in the order given.
- `HandleBeforeCreating.beforeCreatingNewState`: Tells the current state that it will be transitioned away from.
- `HandleAfterCreating.afterCreatingNewState`: Tell the current state that it will transition to a new given state.
- `HandleBeforeOldStateIsRemoved.beforeOldStateIsRemoved`: Tells the new state that is is about to be transitioned to. After this call the new state will become the current state.
- `HandleAfterNewStateIsSet.afterNewStateIsSet`: Tells the previous state that the transition has occurred and it is no longer the current state.
- `HandleAfterOldStateIsRemoved.afterOldStateIsRemoved`: Tells the new state that the previous state was removed.

If an atomic action is required on the state `useState()` can be called. When called, the state is guaranteed not to change for the duration of the action.

#### Hot and Cold Flows
Flowable or stateRepo may have a variable number of flows observing them.
Thus they may at some point (temporarily) lose all observers.
To determine what should happen when no observers are available, both flowable and stateRepo can be marked as either stateful (Hot) or stateless (Cold).

A **Hot** flowable/stateRepo gets an initial value upon creation.
This value remains until the flowable is closed.
Implement `HotFlowable` and `HotStateRepo` respectively to use this behaviour.

A **Cold** flowable/stateRepo by comparison only has a value when at least one flow is observing it.
Upon creation the cold data stream should provide both an initializer and a deinitializer.
The initializer will be called if the number of flows observing changes from 0 to 1.
The deinitializer will be called when the number of flows observing changes drops to 0.
Use `ColdFlowable` and `ColdStateRepo` for this behaviour.
Note that by default `ColdFlowable` closes itself before calling its deinitializer function.

### Date
Kaluga includes a `Date` class to manage and compare time.
Dates can be created using either `Date.now()` or `Date.epoch()`.
Dates are mutable be default and can be compared.

```kotlin
val today = Date.now()
val tomorrow = today.copy().apply {
    day += 1
}
assertTrue(today < tomorrow)
```

### Formating
It's possible to format to and from some data types using Kaluga.

- `Date` can be formatted and parsed using a `DateFormatter`
- `Number` can be formatted and parsed using a `NumberFormatter`
- `String` can be formatted to include different data types using `StringFormatter`


### Monitors
Monitors are cold flow service that monitor the status for a particular system service. In your app before you do something you may need to check whether that service is **on** or **off**,  you can do so by observing a `ServiceMonitorRepo`. You will need to setup a builder per platform and pass it to the common ViewModel to actually create the repo. The builder parameters depends on the platform and on the service. 
Once the builder are available in the ViewModel, the repo is created by calling the `create()` method from the `Builder` which will take a `coroutineContext`. 

At the moment the repo available are Bluetooth and Location and are used as following:
```kotlin
// Android code somewhere
val vm = SomeViewModel(
    BluetoothMonitor.Builder(applicationContext, BluetoothAdapter.getDefaultAdapter()),
    LocationMonitor.Builder(applicationContext)
)
```

```swift
// iOS code somewhere
let vm = ServiceMonitorViewModel(
    BluetoothMonitor.Builder(CBCentralManager()),
    LocationMonitor.Builder(CLLocationManager())
)
```

```kotlin
// Common VM
class SomeViewModel(
    bltMonitorBuilder: BluetoothMonitor.Builder, 
    locationMonitorBuilder: LocationMonitor.Builder
) : BaseViewModel() {
    
    private val bltRepo = bltMonitorBuilder.create(coroutineScope.coroutineContext)
    private val locationRepo = locationMonitorBuilder.create(coroutineScope.coroutineContext)
    
    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)

        coroutineScope.launch {
            bltRepo.collect(::mapValues)
        }

        coroutineScope.launch {
            locationMonitorRepo.collect(::mapValues)
        }
    }

    private fun mapValues(state: ServiceMonitorState) {
        when (state) {
            is ServiceMonitorState.Initialized.Disabled -> "Service is disabled"
            is ServiceMonitorState.Initialized.Enabled -> "Service is enabled"
            is ServiceMonitorState.NotInitialized -> "Service status was not initialized yet"
            ServiceMonitorState.NotSupported -> "Service is not supported (simulator)"
        }
    }
}
```