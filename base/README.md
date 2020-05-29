## Base
This Base Library for Kaluga contains Data types and accessors for easy use across all Kaluga projects.

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