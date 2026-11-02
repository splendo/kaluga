plugins {
    id("com.splendo.kaluga.plugin.ksp")
}

kaluga {
    moduleName = "bytes"
}

dependencies {
    implementation(project(":bytes:annotations"))
    implementation(project(":bytes:layout"))
    testImplementation(project(":bytes:view"))
    testImplementation(project(":bytes:serialization"))
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.kctfork.core)
    testImplementation(libs.kctfork.ksp)
    testImplementation(libs.kotlin.serialization.compiler.plugin.embeddable)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

kotlin {
    compilerOptions {
        optIn.add("com.splendo.kaluga.bytes.layout.InternalBytesApi")
    }
}
