plugins {
    id("com.splendo.kaluga.plugin.ksp")
}

kaluga {
    moduleName = "ksp"
}

dependencies {
    testImplementation(kotlin("test-junit5"))
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
