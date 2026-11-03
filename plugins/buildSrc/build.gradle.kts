plugins {
    `kotlin-dsl`
}

// Compiles the Kaluga build logic's version calculation itself, so the plugins publish at the same version as the library.
sourceSets.main {
    kotlin {
        srcDir("../../kaluga-library-components/src/main/kotlin/helpers")
        include("Versioning.kt")
    }
}

dependencies {
    implementation(libs.palantir.git.version)
}
