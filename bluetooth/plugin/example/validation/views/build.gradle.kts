import com.splendo.kaluga.bluetooth.plugin.BluetoothPluginVersion
import com.splendo.kaluga.bluetooth.plugin.BluetoothTarget
import com.splendo.kaluga.bluetooth.plugin.ImplementFor

plugins {
    id("com.splendo.kaluga.plugin")
    id("com.splendo.kaluga.bytes.plugin")
    id("com.splendo.kaluga.bluetooth.plugin")
}

kaluga {
    moduleName = "bluetooth.validation.views"
    dependencies {
        common {
            test {
                implementation("com.splendo.kaluga.base:test:${BluetoothPluginVersion.kalugaVersion}")
                implementation("com.splendo.kaluga.bluetooth:test-client:${BluetoothPluginVersion.kalugaVersion}")
                implementation("com.splendo.kaluga.bluetooth:test-server:${BluetoothPluginVersion.kalugaVersion}")
            }
        }
    }
}

bluetooth {
    target.set(setOf(BluetoothTarget.CLIENT, BluetoothTarget.SERVER))
    implementFor.set(setOf(ImplementFor.BLUETOOTH, ImplementFor.SIMULATOR, ImplementFor.MOCK))
}

// C1 only: without escape analysis every allocation is counted, whatever the JIT has warmed up.
val androidHostAllocationTest = tasks.register<Test>("androidHostAllocationTest") {
    description = "Checks what collecting the notifications of a view allocates."
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
