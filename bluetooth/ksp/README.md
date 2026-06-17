# Bluetooth KSP

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
|  |  | ✅ |  |  |  |  |  |

The [KSP](https://kotlinlang.org/docs/ksp-overview.html) symbol processor that powers Bluetooth code generation. 
It reads a device described with the [`@Bluetooth` annotations](../annotations/) and generates the typed client and server
APIs (and their `Bluetooth` / `Simulated` implementations) on top of [`bluetooth-core`](../core/),
[`bluetooth-client`](../client/) and [`bluetooth-server`](../server/). 
Being a KSP processor it is a build-time, JVM-only artifact and produces no runtime library of its own.

It is recommended you do not apply this processor yourself. The [`com.splendo.kaluga.bluetooth.plugin`](../plugin/) registers it
on the relevant KSP configurations and passes it the generation options (target roles, implementations, packages) from
the `bluetooth { }` DSL. See the [code generation plugin README](../plugin/) for how to configure generation, and the
[`example/`](../plugin/example/) composite for validation fixtures exercising every capability.
