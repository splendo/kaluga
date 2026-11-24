plugins {
    id("com.splendo.kaluga.plugin.ksp")
}

kaluga {
    moduleName = "bluetooth"
}

dependencies {
    implementation(project(":bluetooth:annotations"))
    implementation(project(":ksp:processor-support"))
    testImplementation(kotlin("test-junit5"))
    testImplementation(project(":bytes:annotations"))
    testImplementation(project(":bytes:ksp"))
    testImplementation(project(":bytes:serialization"))
    testImplementation(project(":bytes:view"))
    testImplementation(libs.kctfork.core)
    testImplementation(libs.kctfork.ksp)
    testImplementation(libs.kotlin.serialization.compiler.plugin.embeddable)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
