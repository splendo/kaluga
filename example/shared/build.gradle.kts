import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family

plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
    id(libs.plugins.compose.get().pluginId)
    // Aggregates the Compose resources (the Material Icons font in :core-arch) into the iOS/macOS frameworks.
    alias(libs.plugins.jetbrains.compose)
}

/** Mobile-only feature modules. Wired into iOS + Android variants of the framework (not macOS
 *  — they have no macOS targets) and exported only from the iOS framework, since Swift
 *  constructs their ViewModels directly (Compose isn't used for these features on iOS). */
val mobileFeatureProjects = listOf(
    ":feature-alerts",
    ":feature-architecture",
    ":feature-datetimepicker",
    ":feature-hud",
    ":feature-keyboard",
    ":feature-resources",
)

/** Kaluga library modules whose types appear in the public API of the mobile-feature ViewModels
 *  Swift consumes (`Navigator<X>` constructor parameters, `AlertPresenter.Builder`, etc.).
 *  Exported only on iOS for the same reason as `mobileFeatureProjects`. */
val mobileKalugaModules = listOf(
    "alerts",
    "architecture",
    "base",
    "date-time-picker",
    "hud",
    "keyboard",
    "resources",
)

/** Feature modules supported on macOS/iOS/Android but with no `wasmJs` target (their Kaluga libraries
 *  have no web support): Web Bluetooth has no server role, no advertisement scanning for beacons, and
 *  the web has no app-store review. Kept off `:shared`'s common source set so it stays wasmJs-capable. */
val nonWebFeatureProjects = listOf(
    ":feature-beacons",
    ":feature-bluetooth-server",
    ":feature-review",
)

kaluga {
    moduleName = "example.shared"
    supportMacOS = true
    supportWasmJS = true
    appleFramework {
        baseName = "KalugaExample"
        isStatic = false
        // `transitiveExport = false`: only the projects + modules explicitly listed below appear
        // in the framework's public Obj-C header. This is both architectural hygiene (the
        // macOS-capable features render their UI inside Compose, so Swift never references their
        // types) and the only practical defence against `kaluga.scientific.unit.Pascal` leaking
        // in transitively — its `+pascal` selector collides with clang's reserved `pascal`
        // calling-convention keyword under Xcode 26.x.
        transitiveExport = false
        // Cross-platform foundation — used by Swift on both iOS and macOS hosts.
        export(project(":core-arch"))
        export(project(":core-koin"))
        // `CoroutineScope` and friends turn up in the generated KalugaSwiftUI bindings
        // (KeyboardManager.create(coroutineScope:), Subject helpers, etc.) and in many Kaluga
        // ViewModel signatures. With `transitiveExport = false` we have to export
        // `kotlinx-coroutines-core` explicitly or Swift cannot resolve the type.
        export(libs.kotlinx.coroutines.core)
        // macOS-capable feature modules render entirely in Compose; their classes stay linked in
        // the framework (so their Koin contributions register at startup) but are *not* exported
        // — Swift never references `BluetoothListScreen`, `LocationScreen`, `LinksScreen`, etc.
    }
    dependencies {
        android {
            main {
                mobileFeatureProjects.forEach { api(project(it)) }
                nonWebFeatureProjects.forEach { api(project(it)) }
            }
        }
        ios {
            main {
                mobileFeatureProjects.forEach { api(project(it)) }
            }
        }
        apple {
            main {
                // macOS + iOS get the non-web features; the web (`common`) source set must not see them.
                nonWebFeatureProjects.forEach { api(project(it)) }
            }
        }
        common {
            main {
                api(project(":core-arch"))
                api(project(":core-koin"))
                // For `ProvideNSWindow` at the macOS entry point + `AttachToCompose` extensions.
                api("com.splendo.kaluga.lifecycle:compose:${project.rootProject.version}")
                api(project(":feature-bluetooth-client"))
                api(project(":feature-datetime"))
                api(project(":feature-links"))
                api(project(":feature-localization"))
                api(project(":feature-location"))
                api(project(":feature-media"))
                api(project(":feature-permissions"))
                // `:feature-scientific` is linked-but-hidden via `implementation` so its public
                // types (QuantityDetails, converters) — which expose `kaluga.scientific.unit.*`
                // in their signatures — never leak into the framework header. Scientific UI is
                // Compose-only; Swift never references its types.
                implementation(project(":feature-scientific"))
                api(project(":feature-system"))
            }
        }
    }
}

// iOS-only exports: mobile feature modules (their Swift-consumed ViewModels) + the Kaluga
// libraries whose types appear in those ViewModels' public APIs. `appleFramework {}` runs the
// same lambda for both iOS and macOS frameworks, so target-specific exports are added here.
afterEvaluate {
    kotlin.targets.withType<KotlinNativeTarget>().configureEach {
        if (konanTarget.family == Family.IOS) {
            binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework>().configureEach {
                mobileFeatureProjects.forEach { export(project(it)) }
                mobileKalugaModules.forEach {
                    export("com.splendo.kaluga:$it:${project.rootProject.version}")
                }
            }
        }
    }
}

// ktlint (lintKotlin/formatKotlin) would otherwise scan the generated Compose resource accessors
// aggregated from dependencies (e.g. the Material Icons font in :core-arch).
tasks.withType<org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask>().configureEach {
    exclude { it.file.path.contains("/generated/") }
}
