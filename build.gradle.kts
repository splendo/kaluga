plugins {
    id("com.splendo.kaluga.plugin")
    id("rs.houtbecke.gradle.recorder.plugin")
}

apply(from = "gradle/newModule.gradle.kts")
apply(from = "gradle/copyReports.gradle.kts")
