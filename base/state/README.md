# Base State

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

This library provides Kaluga's state-machine primitives: `KalugaState` and the `StateRepo` family.

## Installing
This library is available on Maven Central. You can import Kaluga Base State as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.base:state:$kalugaVersion")
}
```

## State
The Kaluga library offers usage of State Machines.

The `StateRepo` functions as a `Flow` to manage a state machine.
The states the state repo can be in are described by a `KalugaState` implementation.
However, in order to ensure only acceptable state transitions occur it is not possible to directly set a new state.
Instead `takeAndChangeState()` should be called.
This method takes a closure that determines the next state based on the current state.
The closure should either return a new state created from the given state or `remain` to cancel state transitions.
If additional calls to takeAndChangeState occur while transitioning to a new state they will be enqueued until the pending transition has completed.

It is recommended to only allow `KalugaState` classes to create the new state that they can transition into.

```kotlin
sealed class SomeState : KalugaState<SomeState> {

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

### Hot and Cold State Repos
StateRepo may have a variable number of flows observing them.
Thus they may at some point (temporarily) lose all observers.
To determine what should happen when no observers are available, both flowable and stateRepo can be marked as either stateful (Hot) or stateless (Cold).

A **Hot** stateRepo gets an initial value upon creation.
This value remains until the flowable is closed.
Implement `HotStateRepo` respectively to use this behaviour.

A **Cold** stateRepo by comparison only has a value when at least one flow is observing it.
Upon creation the cold data stream should provide both an initializer and a deinitializer.
The initializer will be called if the number of flows observing changes from 0 to 1.
The deinitializer will be called when the number of flows observing changes drops to 0.
Use `ColdStateRepo` for this behaviour.
