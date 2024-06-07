plugins {
    id("com.splendo.kaluga.plugin.android.compose")
}

kaluga {
    moduleName = "keyboard"
}

dependencies {
    implementation(project(":base"))
    api(project(":keyboard"))
    api(project(":architecture-compose"))
}
