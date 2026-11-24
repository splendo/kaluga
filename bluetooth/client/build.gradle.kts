plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
}

kaluga {
    moduleName = "bluetooth.client"
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    dependencies {
        android {
            main {
                implementation(libs.nordic.support.scanner)
                api(project(":location:location", ""))
                implementation(libs.kotlinx.atomicfu)
            }
        }
        ios {
            main {
                implementation(libs.kotlinx.atomicfu)
            }
        }
        common {
            main {
                api(project(":bluetooth:core"))
                api(project(":base:state", ""))
                api(project(":date-time:date-time"))
                api(project(":permissions:bluetooth", ""))
            }
            test {
                implementation(project(":bluetooth:test-client", ""))
                implementation(project(":permissions:test", ""))
            }
        }
    }
}

// C1 only: without escape analysis every allocation is counted, whatever the JIT has warmed up.
val androidHostAllocationTest = tasks.register<Test>("androidHostAllocationTest") {
    description = "Checks what notifying a characteristic's subscribers allocates."
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
