plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "bytes.view"

    supportJVM = true
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true

    dependencies {
        common {
            main {
                api(project(":bytes:annotations", ""))
                api(project(":bytes:core", ""))
                api(project(":bytes:layout", ""))
                api(project(":bytes:medfloat", ""))
                api(project(":bytes:strings", ""))
                api(project(":bytes:stuffing", ""))
            }
            test {
                implementation(project(":base:test", ""))
                implementation(project(":bytes:crc", ""))
                implementation(project(":bytes:integer", ""))
            }
        }
    }
}

tasks.named<Test>("jvmTest") {
    filter { excludeTestsMatching("*AllocationTest") }
}

// C1 only: without escape analysis every allocation the code asks for is counted, whatever the JIT has warmed up.
// It reads the compiled JVM tests, not the jvmTest task, so running it does not run every JVM test.
val jvmAllocationTest = tasks.register<Test>("jvmAllocationTest") {
    description = "Checks what the view runtime allocates per call."
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    val compilation = kotlin.jvm().compilations.getByName("test")
    testClassesDirs = compilation.output.classesDirs
    classpath = compilation.output.allOutputs + (compilation.runtimeDependencyFiles ?: files())
    filter { includeTestsMatching("*AllocationTest") }
    jvmArgs("-XX:TieredStopAtLevel=1")
}

tasks.matching { it.name == "allTests" || it.name == "testAndroidHostTest" }.configureEach { dependsOn(jvmAllocationTest) }

kotlin {
    sourceSets.all {
        languageSettings.optIn("com.splendo.kaluga.bytes.layout.InternalBytesApi")
    }
}
