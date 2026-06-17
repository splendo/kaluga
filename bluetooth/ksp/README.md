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

## Advanced: using the processor directly

The [code generation plugin](../plugin/) is the recommended way to use this processor as it automatically applies KSP, registers the
processor on every compilation, adds the runtime dependencies, and wires the generated sources for you. 
Use for the processor (`com.splendo.kaluga.bluetooth:ksp`) directly only when your build can't accommodate that wiring, for example
a complicated multiplatform source-set hierarchy or when you need to own the KSP and target configuration yourself. This
assumes you are already comfortable wiring up a KSP processor.

Two things are specific to this processor when wiring it up yourself:

- **Runtime dependencies** must be on the classpath of the source set holding your definitions:
  `com.splendo.kaluga.bluetooth:annotations` and `:core` always, plus `:client` and/or `:server` when generating their
  implementations.
- **Generated code** is emitted under `build/generated/ksp/metadata/commonMain/kotlin`, which must be added as a source
  directory.

Generation is configured through KSP options (`ksp { arg("name", "value") }`), which mirror the `bluetooth { }` DSL:

| Option | Values | Description |
|---|---|---|
| `target` | `CLIENT`, `SERVER` (comma-separated) | Roles to generate. |
| `implementFor` | `BLUETOOTH`, `SIMULATOR` (comma-separated) | Implementations to generate; omit for API-only. |
| `generateApi` | `true` / `false` | Whether to generate the API interfaces (`false` imports them from another module). |
| `generatedPackage` | package name | Package for the generated code (defaults to the definitions' package). |
| `apiPackage` | package name | Package the generated API interfaces live in (defaults to `generatedPackage`). |
| `commonSource` | `:`-separated source dirs | The common source directories holding the definitions; used in a multiplatform build to generate shared definitions once (in the metadata pass) instead of once per target. |
| `isSingleTarget` | `true` / `false` | `true` for a single-target build; when `false` the processor uses `commonSource` to avoid generating common definitions per target. |
