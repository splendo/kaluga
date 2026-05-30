import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family

plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
    id(libs.plugins.compose.get().pluginId)
}

/** Project deps wired only into the iOS + Android variants of the framework — these modules
 *  target Android+iOS only (no macOS), so they cannot be declared in `common` or `apple`. */
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

kaluga {
    moduleName = "example.shared"
    supportMacOS = true
    appleFramework {
        baseName = "KalugaExample"
        isStatic = false
        transitiveExport = true
        export(project(":core-arch"))
        export(project(":core-koin"))
        export(project(":feature-bluetooth"))
        export(project(":feature-datetime"))
        export(project(":feature-info"))
        export(project(":feature-links"))
        export(project(":feature-location"))
        export(project(":feature-permissions"))
        export(project(":feature-scientific"))
        export(project(":feature-system"))
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
                api(project(":feature-scientific"))
                api(project(":feature-system"))
            }
        }
    }
}

// Mobile feature modules have no macOS target, so they can only be exported from the iOS
// variant of the single KalugaExample framework. `appleFramework {}` runs the same lambda for
// both iOS and macOS frameworks, so the iOS-only exports are added here after the kaluga plugin
// has created the framework binaries in its own afterEvaluate.
afterEvaluate {
    kotlin.targets.withType<KotlinNativeTarget>().configureEach {
        if (konanTarget.family == Family.IOS) {
            binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework>().configureEach {
                mobileFeatureProjects.forEach { export(project(it)) }
            }
        }
    }
}
