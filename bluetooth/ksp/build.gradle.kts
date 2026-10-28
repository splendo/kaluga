plugins {
    id("com.splendo.kaluga.plugin.ksp")
}

kaluga {
    moduleName = "bluetooth"
}

dependencies {
    implementation(project(":bluetooth:annotations"))
    testImplementation(kotlin("test-junit5"))
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
