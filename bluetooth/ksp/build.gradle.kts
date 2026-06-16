plugins {
    id("com.splendo.kaluga.plugin.ksp")
}

kaluga {
    moduleName = "bluetooth"
}

dependencies {
    implementation(project(":bluetooth:annotations"))
}
