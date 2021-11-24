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
`RecurringTimer` is a timer which once started provides ticks at regular intervals.

### Example
Instantiate and operate a timer

```kotlin
// creates a timer which lasts for 1 minute with a tick every second
val timer = RecurringTimer(
    duration = Duration.minutes(1),
    inteval = Duration.seconds(1)
)
// the timer is created in a paused state. starting the timer..
timer.start()
// collecting results and printing the output
timer.elapsed().collect { elapsed: Duration ->
    println("${elapsed.inWholeMilliseconds} ms")
    // simulate some work
    delay(100)
}
```
The timer implementation makes the best effort to compensate the inaccuracies in intervals so might 
look in the following way:
```kotlin
12 ms  // an initial "warm up delay" 
1001 ms // overshoot by 1 ms
2005 ms // overshoot by 5 ms
2999 ms // undershoot by 1 ms
4003 ms // overshoot by 3 ms
...
60000 ms
```
