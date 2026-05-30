import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family

plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
    id(libs.plugins.compose.get().pluginId)
}

/** Mobile-only feature modules. Wired into iOS + Android variants of the framework (not macOS
 *  — they have no macOS targets) and exported only from the iOS framework, since Swift
 *  constructs their ViewModels directly (Compose isn't used for these features on iOS). */
val mobileFeatureProjects = listOf(
    ":feature-alerts",
    ":feature-architecture",
    ":feature-beacons",
    ":feature-datetimepicker",
    ":feature-hud",
    ":feature-keyboard",
    ":feature-media",
    ":feature-resources",
)

/** Kaluga library modules whose types appear in the public API of the mobile-feature ViewModels
 *  Swift consumes (`Navigator<X>` constructor parameters, `AlertPresenter.Builder`, etc.).
 *  Exported only on iOS for the same reason as `mobileFeatureProjects`. */
val mobileKalugaModules = listOf(
    "alerts",
    "architecture",
    "base",
    "beacons",
    "date-time-picker",
    "hud",
    "keyboard",
    "media",
    "resources",
)

kaluga {
    moduleName = "example.shared"
    supportMacOS = true
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
            }
        }
        ios {
            main {
                mobileFeatureProjects.forEach { api(project(it)) }
            }
        }
        common {
            main {
                api(project(":core-arch"))
                api(project(":core-koin"))
                api(project(":feature-bluetooth"))
                api(project(":feature-datetime"))
                api(project(":feature-info"))
                api(project(":feature-links"))
                api(project(":feature-location"))
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
