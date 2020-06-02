## Base
This Base Library for Kaluga contains Data types and accessors for easy use across all Kaluga projects.

### Threading
You can run code on the OS Main Thread by using `runOnMain`. Use the `MainQueueDispatcher` to access the Main queue of the OS.

### Data Accessors
- Use `byOrdinalOrDefault` to get an Enum value by ordinal or the default value if no such value exists.
- (Android Only) Use the `ApplicationHolder` class to get and set the current `Application`. This is useful for default access to the ApplicationContext

### Flowable and State
The Kaluga library offers usage of Flows and State Machines. Both offer access to a `flow` method, which provides a new `Flow` to monitor and access data. A Flowable or State Machine may have multiple observers to its flow. Because of this both can be either Hot or Cold.
A Hot Flow/State contains data only if there is at least one flow being observed/collected.
A Cold Flow/State contains data regardless of the number of observers.