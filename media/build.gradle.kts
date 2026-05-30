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
                // api because `MediaSurfaceProvider` extends `LifecycleSubscribable` — consumers
                // need to be able to resolve that supertype.
                api(project(":lifecycle"))
                implementation(project(":logging"))
            }
            test {
                implementation(project(":test-utils-media"))
            }
        }
    }
}
