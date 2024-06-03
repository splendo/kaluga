plugins {
    id("kaluga-library-components")
    id("rs.houtbecke.gradle.recorder.plugin")
}

apply(from = "gradle/newModule.gradle.kts")
apply(from = "gradle/copyReports.gradle.kts")
