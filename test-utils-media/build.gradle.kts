plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.media"
    supportMacOS = true
    dependencies {
        common {
            main {
                api(project(":media"))
                // `:test-utils-architecture` (the previous dep) is unused — verified: nothing in
                // `:test-utils-media` or `:media`'s tests imports `com.splendo.kaluga.architecture`
                // or `com.splendo.kaluga.test.architecture`. The only thing actually needed
                // transitively was `:test-utils-base` (mock framework + `kotlin.test`), so we
                // declare it directly. Dropping `:test-utils-architecture` lets `:test-utils-media`
                // grow macOS support since `:test-utils-base` already targets it.
                api(project(":test-utils-base"))
            }
        }
    }
}
