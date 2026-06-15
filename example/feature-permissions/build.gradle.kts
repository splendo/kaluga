plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "example.feature.permissions"
    supportMacOS = true
    supportWasmJS = true

    dependencies {
        android {
            main {
                api(libs.koin.compose.viewmodel)
                api("com.splendo.kaluga.architecture:architecture:${project.rootProject.version}")
                // Android demonstrates every permission type.
                api("com.splendo.kaluga.permissions:bluetooth:${project.rootProject.version}")
                api("com.splendo.kaluga.permissions:calendar:${project.rootProject.version}")
                api("com.splendo.kaluga.permissions:camera:${project.rootProject.version}")
                api("com.splendo.kaluga.permissions:contacts:${project.rootProject.version}")
                api("com.splendo.kaluga.permissions:location:${project.rootProject.version}")
                api("com.splendo.kaluga.permissions:microphone:${project.rootProject.version}")
                api("com.splendo.kaluga.permissions:storage:${project.rootProject.version}")
            }
        }
        apple {
            main {
                api("com.splendo.kaluga.permissions:bluetooth:${project.rootProject.version}")
                api("com.splendo.kaluga.permissions:calendar:${project.rootProject.version}")
                api("com.splendo.kaluga.permissions:camera:${project.rootProject.version}")
                api("com.splendo.kaluga.permissions:contacts:${project.rootProject.version}")
                api("com.splendo.kaluga.permissions:location:${project.rootProject.version}")
                api("com.splendo.kaluga.permissions:microphone:${project.rootProject.version}")
                api("com.splendo.kaluga.permissions:storage:${project.rootProject.version}")
            }
        }
        wasmJs {
            main {
                // The browser only exposes geolocation, notifications, camera, microphone and Bluetooth.
                api("com.splendo.kaluga.permissions:bluetooth:${project.rootProject.version}")
                api("com.splendo.kaluga.permissions:camera:${project.rootProject.version}")
                api("com.splendo.kaluga.permissions:location:${project.rootProject.version}")
                api("com.splendo.kaluga.permissions:microphone:${project.rootProject.version}")
            }
        }
        common {
            main {
                api(project(":core-arch"))
                api(project(":core-koin"))
                // `core` (PermissionsBuilder/Permission) + `notifications` (NotificationOptions) are used
                // by common code; per-type modules are added on the platforms that demonstrate them.
                api("com.splendo.kaluga.permissions:core:${project.rootProject.version}")
                api("com.splendo.kaluga.permissions:notifications:${project.rootProject.version}")
            }
        }
    }
}
