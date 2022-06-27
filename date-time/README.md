# Date Time

This library contains multiplatform classes to work with date and time.

## Installing
This library is available on Maven Central. You can import Kaluga Date Time as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:date-time:$kalugaVersion")
}
```

## Usage

### RecurringTimer
`RecurringTimer` is a timer which once started provides ticks at regular intervals.

Sample code to instantiate and operate a timer:

```kotlin
// creates a timer which lasts for 1 minute with a tick every second
val timer = RecurringTimer(
    duration = 1.minutes,
    interval = 1.seconds
)
// the timer is created in a paused state. starting the timer..
timer.start()
// collecting results and printing the output
timer.elapsed().collect { elapsed: Duration ->
    println("${elapsed.inWholeMilliseconds} ms")
    // simulate some work
    delay(100)
}
println("Done!")
```

The timer implementation makes the best effort to compensate the inaccuracies in intervals. 
In addition, one or several frames will be skipped in case the consumer processing takes longer than
[interval]. 
A running timer automatically changes to a [Finished] state once [duration] elapses.
[Finished] is a terminal state and following [start] calls would have no effect. 
The elapsed time is always within [0, duration] range and equal to [duration] in [Finished] state. 

So the output might look the following way:
```kotlin
12 ms  // an initial "warm up delay" 
1001 ms // overshoot by 1 ms
3005 ms // skipped a frame due to slow processing and overshoot by 5 ms
3999 ms // undershoot by 1 ms
5003 ms // overshoot by 3 ms
...
60000 ms 
Done!
```
