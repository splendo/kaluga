plugins {
    id("com.splendo.kaluga.plugin.android.compose")
}

kaluga {
    moduleName = "keyboard"
}

dependencies {
    implementation(project(":base:core"))
    api(project(":keyboard:keyboard"))
    api(project(":architecture:compose"))
}
