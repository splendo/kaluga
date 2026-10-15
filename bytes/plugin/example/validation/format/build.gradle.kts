import com.splendo.kaluga.bytes.plugin.BytesPluginVersion
import com.splendo.kaluga.bytes.plugin.Generate

plugins {
    id("com.splendo.kaluga.plugin")
    id("com.splendo.kaluga.bytes.plugin")
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "bytes.validation.format"

    // Generated views run on every platform the library supports, so the suites run on the web targets too.
    supportJS = true
    supportWasmJS = true

    dependencies {
        common {
            main {
                implementation("com.splendo.kaluga.scientific:scientific:${BytesPluginVersion.kalugaVersion}")
                // The scientific units implement its Serializable, which the web compilations need on their classpath.
                implementation("com.splendo.kaluga.base:core:${BytesPluginVersion.kalugaVersion}")
            }
            test {
                implementation("com.splendo.kaluga.base:test:${BytesPluginVersion.kalugaVersion}")
            }
        }
    }
}

bytes {
    generate.set(setOf(Generate.DATA, Generate.VIEW))
    // The suite covers every feature, while VIEW generation grows phase by phase.
    skipUnsupportedViews.set(true)
}

// C1 only: without escape analysis every allocation the generated views ask for is counted, whatever the JIT has warmed up.
val androidHostAllocationTest = tasks.register<Test>("androidHostAllocationTest") {
    description = "Checks what the generated views allocate per call."
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    val hostTest = tasks.named<Test>("testAndroidHostTest").get()
    testClassesDirs = hostTest.testClassesDirs
    classpath = hostTest.classpath
    filter { includeTestsMatching("*AllocationTest") }
    jvmArgs("-XX:TieredStopAtLevel=1")
}

tasks.withType<Test>().matching { it.name == "testAndroidHostTest" }.configureEach {
    filter { excludeTestsMatching("*AllocationTest") }
    dependsOn(androidHostAllocationTest)
}
tasks.matching { it.name == "allTests" }.configureEach { dependsOn(androidHostAllocationTest) }
