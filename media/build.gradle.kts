plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "media"
    supportMacOS = true
    dependencies {
        common {
            main {
                implementation(project(":base"))
                // api: `MediaSurfaceProvider` extends `LifecycleSubscribable`.
                api(project(":lifecycle"))
                implementation(project(":logging"))
            }
            test {
                implementation(project(":test-utils-media"))
            }
        }
    }
}
